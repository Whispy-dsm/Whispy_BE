# Backend Implementation Patterns

## 필수 구현 규칙

- Service를 생성할 때는 단위 테스트를 작성합니다.
- Controller를 생성하거나 수정할 때는 `global/document/api/{도메인}/`의 ApiDocument 인터페이스도 구현하거나 수정합니다.
- 새 예외를 추가할 때는 `ErrorCode` enum에 코드를 추가한 뒤 예외 클래스를 생성합니다.
- 클래스, 인터페이스, enum, record, 생성자, 메서드 위에는 Javadoc을 작성합니다.
- 내부 helper 메서드도 예외 없이 Javadoc을 작성합니다.

## 네이밍 컨벤션

- UseCase: `{동작}{엔티티}UseCase`, 예: `SaveFocusSessionUseCase`
- Service: `{동작}{엔티티}Service`, 예: `SaveFocusSessionService`
- Controller: `{엔티티}Controller`
- Port: `{동작}{엔티티}Port`, `Query{엔티티}Port`, `Save{엔티티}Port`, `Delete{엔티티}Port`
- Adapter: `{엔티티}PersistenceAdapter` 또는 `{외부시스템}Adapter`

패키지 규칙:

- 도메인 단위 패키지를 우선합니다.
- 패키지명은 역할보다 도메인을 먼저 드러냅니다.
- DTO는 `request`, `response` 하위 패키지에 둡니다.
- 이름만 보고 책임과 위치를 유추할 수 있어야 합니다.

## 도메인 모델 규칙

- 도메인 모델은 가능하면 `record`와 `@Aggregate`를 사용합니다.
- 불변성 검증은 compact constructor 또는 도메인 메서드에서 처리합니다.
- 상태 변경은 setter보다 새 인스턴스를 반환하는 메서드로 표현합니다.
- JPA entity를 도메인 모델처럼 직접 사용하지 않습니다.
- 비즈니스 규칙을 Controller request DTO에만 두지 않습니다.

## 예외 처리 패턴

일반 예외는 싱글톤 패턴을 사용합니다.

```java
public class FocusSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new FocusSessionNotFoundException();

    private FocusSessionNotFoundException() {
        super(ErrorCode.FOCUS_SESSION_NOT_FOUND);
    }
}
```

사용 예시:

```java
throw FocusSessionNotFoundException.EXCEPTION;
```

외부 시스템 연동 실패처럼 원인 추적이 필요한 경우에는 cause 생성자를 제공합니다.

```java
public class EmailSendFailedException extends WhispyException {
    public EmailSendFailedException(Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, cause);
    }
}
```

새 예외 추가 순서:

1. `global/exception/error/ErrorCode.java`에 에러 코드를 추가합니다.
2. `global/exception/domain/{도메인}/`에 예외 클래스를 생성합니다.
3. 외부 시스템 연동 예외는 cause 생성자를 제공합니다.

HTTP 상태 코드 기준:

- 잘못된 입력: `400 Bad Request`
- 인증 실패: `401 Unauthorized`
- 리소스 없음: `404 Not Found`
- 중복 또는 충돌: `409 Conflict`
- 서버 내부 오류: `500 Internal Server Error`

세부 규칙:

- 클라이언트 입력 문제는 `500`으로 흘리지 않습니다.
- JSON 역직렬화 실패는 `400`으로 처리합니다.

## API 문서화 패턴

Controller는 thin controller 원칙을 따릅니다.

- 요청 검증, 응답 반환, UseCase 호출만 남깁니다.
- 비즈니스 로직과 조립 로직은 service 또는 assembler로 보냅니다.

Controller는 `global/document/api/{도메인}/`의 인터페이스를 구현하여 Swagger 문서를 분리합니다.

```java
@RestController
public class FocusSessionController implements FocusSessionApiDocument {
    // 구현
}
```

## AOP 로깅 패턴

Service 메서드에는 성격에 맞는 액션 로깅 어노테이션을 사용합니다.

```java
@UserAction("집중 세션 저장")
public FocusSessionResponse execute(SaveFocusSessionRequest request) { ... }
```

```java
@AdminAction("공지사항 생성")
public void execute(CreateAnnouncementRequest request) { ... }
```

```java
@SystemAction("구독 만료 알림 배치")
public void notifyExpiredSubscriptions() { ... }
```

로그 포맷:

- `[USER_ACTION] userId: {id} - {action} - params: {parameters}`
- `[ADMIN_ACTION] adminId: {id} - {action} - params: {parameters}`
- `[SYSTEM_ACTION] {action} - params: {parameters} - SUCCESS (100ms)`

## 기능 문서화 규칙

- 기능 요구사항은 한 문서에 여러 도메인을 섞어 쓰지 않습니다.
- PRD 문서는 기능 단위로 쪼갭니다.
- 관련 기능은 도메인 기준으로 묶어 다시 볼 수 있게 정리합니다.

## 로깅 설정

- 로깅 레벨과 appender 설정은 `logback-spring.xml`에서 관리합니다.
- `application.yml`의 `jpa.show-sql`은 각 환경별 yml에서 관리합니다.
- 로깅 레벨을 `application.yml`에 중복 정의하지 않습니다.
- 프로덕션 로그 파일은 90일 보관합니다.
- 일반 로그는 최대 10GB, 에러 로그는 최대 3GB까지 보관합니다.

환경별 기준:

- `local`: 애플리케이션 DEBUG, Hibernate SQL DEBUG, SQL binder TRACE
- `test`: 애플리케이션 DEBUG, Hibernate SQL DEBUG, SQL binder TRACE
- `prod,stag`: 애플리케이션 DEBUG, Hibernate SQL WARN, FILE과 ERROR_FILE appender 사용
