# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

Whispy_BE는 수면, 집중, 명상 웰니스 애플리케이션을 위한 Spring Boot 백엔드입니다. Java 21 + Spring Boot 3.4.5 기반이며, 헥사고날 아키텍처(Ports & Adapters)를 적용했습니다.

## 워크플로우 규칙

### 분석 우선, 편집은 나중에

**절대 규칙: 분석이 완료되기 전에 코드를 편집하지 마세요.**

모든 코드 리뷰, 성능 분석, 아키텍처 검토 작업은 다음 2단계 프로세스를 따라야 합니다:

**Phase 1 - 분석 및 문서화 (편집 금지)**
1. 관련된 모든 파일을 철저히 읽고 분석
2. 발견한 모든 내용을 마크다운 문서로 작성:
   - 파일 경로와 라인 넘버 명시
   - 문제점 또는 개선 사항 설명
   - 심각도 표시 (Critical/High/Medium/Low)
   - 제안하는 수정 방법
3. 분석 문서를 사용자에게 제시
4. **사용자 승인을 기다림 (이 단계에서 절대 파일을 수정하지 않음)**

**Phase 2 - 수정 적용 (승인 후에만)**
1. 사용자가 승인한 항목만 수정
2. 한 번에 하나씩 수정 적용
3. 각 수정 후 관련 테스트 실행
4. 문제 발생 시 즉시 보고

**예시 - JPA 인덱스 리뷰:**
```
❌ 잘못된 방식:
- 엔티티 파일을 읽고 바로 @Index 추가

✅ 올바른 방식:
- 모든 엔티티와 Repository 분석
- INDEX_ANALYSIS.md 문서 생성 (파일 경로, 현재 인덱스, 누락된 인덱스, 쿼리 패턴 포함)
- 사용자에게 문서 제시 및 승인 대기
- 승인 후 엔티티별로 순차적으로 수정
```

**예시 - 성능 최적화:**
```
❌ 잘못된 방식:
- 코드를 읽고 바로 리팩토링 시작

✅ 올바른 방식:
- 전체 코드베이스 분석
- PERFORMANCE_ANALYSIS.md 생성 (병목 지점, 측정 결과, 최적화 제안 포함)
- 사용자 승인 후 우선순위에 따라 수정
```

**이 규칙이 적용되는 경우:**
- 코드 리뷰
- 데이터베이스 인덱스 분석
- 성능 최적화
- 아키텍처 검토
- 보안 감사
- 리팩토링 제안

**이 규칙의 예외:**
- 사용자가 명시적으로 즉시 수정을 요청한 경우
- 단순한 버그 수정 (오타, 명확한 로직 오류 등)
- 테스트 코드 작성

## 개발 명령어

```bash
# Linux/Mac
./gradlew build                                    # 프로젝트 빌드
./gradlew test                                     # 모든 테스트 실행
./gradlew test --tests ClassName                   # 특정 테스트 클래스 실행
./gradlew test --tests ClassName.testMethodName    # 단일 테스트 메서드 실행
./gradlew bootRun                                  # 애플리케이션 실행

# Windows
gradlew.bat build
gradlew.bat test --tests ClassName
```

## 아키텍처

### 헥사고날 아키텍처 (Ports & Adapters)

```
domain/{도메인명}/
├── adapter/
│   ├── in/web/              # REST 컨트롤러, Request/Response DTO
│   └── out/
│       ├── entity/          # JPA 엔티티
│       ├── mapper/          # MapStruct 매퍼 (Entity ↔ Domain)
│       ├── persistence/     # Repository 어댑터
│       └── external/        # 외부 API 클라이언트
├── application/
│   ├── port/
│   │   ├── in/              # UseCase 인터페이스
│   │   └── out/             # Port 인터페이스
│   └── service/             # UseCase 구현체
├── facade/                  # 횡단 관심사를 위한 파사드
└── model/                   # 도메인 모델 (@Aggregate, Record 기반)
    └── types/               # 도메인별 Enum
```

### 핵심 어노테이션 및 패턴

- `@Aggregate`: 도메인 모델 표시 (Record 기반 불변 객체)
- UseCase 인터페이스: 각 기능별 명시적 인터페이스 (`SaveFocusSessionUseCase` 등)
- Port/Adapter: 의존성 역전을 통한 도메인 격리

### 도메인 모델 특징

Record의 Compact Constructor에서 도메인 규칙 검증:
```java
@Aggregate
public record FocusSession(...) {
    public FocusSession {
        if (endedAt.isBefore(startedAt)) {
            throw InvalidFocusSessionTimeRangeException.EXCEPTION;
        }
    }
}
```

### 예외 처리 패턴

**1) 일반 예외 (싱글톤 패턴)**
```java
public class FocusSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new FocusSessionNotFoundException();

    private FocusSessionNotFoundException() {
        super(ErrorCode.FOCUS_SESSION_NOT_FOUND);
    }
}
// 사용: throw FocusSessionNotFoundException.EXCEPTION;
```

**2) 원인 예외를 포함하는 경우 (Cause 패턴)**

외부 시스템 연동(SMTP, API 호출 등) 실패 시 원인 추적을 위해 cause 사용:
```java
public class EmailSendFailedException extends WhispyException {
    public EmailSendFailedException(Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, cause);
    }
}
// 사용:
// try { ... }
// catch (Exception e) { throw new EmailSendFailedException(e); }
```

새 예외 추가 시:
1. `global/exception/error/ErrorCode.java`에 에러 코드 추가
2. `global/exception/domain/{도메인}/`에 예외 클래스 생성
3. 외부 시스템 연동 예외는 cause 생성자 제공

### API 문서화 패턴

Controller는 `global/document/api/{도메인}/` 내 인터페이스를 구현하여 Swagger 문서 분리:
```java
@RestController
public class FocusSessionController implements FocusSessionApiDocument {
    // 구현
}
```

### AOP 로깅 패턴

Service 메서드에 액션 로깅 어노테이션 사용:

**@UserAction** - 사용자 행동 로깅 (userId 자동 기록)
```java
@UserAction("집중 세션 저장")
public FocusSessionResponse execute(SaveFocusSessionRequest request) { ... }
```

**@AdminAction** - 관리자 행동 로깅 (adminId 자동 기록)
```java
@AdminAction("공지사항 생성")
public void execute(CreateAnnouncementRequest request) { ... }
```

**@SystemAction** - 시스템 이벤트 로깅 (배치, 스케줄러 등, 실행시간 측정)
```java
@SystemAction("구독 만료 알림 배치")
public void notifyExpiredSubscriptions() { ... }
```

로그 포맷:
- `[USER_ACTION] userId: {id} - {action} - params: {parameters}`
- `[ADMIN_ACTION] adminId: {id} - {action} - params: {parameters}`
- `[SYSTEM_ACTION] {action} - params: {parameters} - SUCCESS (100ms)`

### 로깅 설정

로깅 레벨 및 Appender 설정은 **`logback-spring.xml`에서 관리**합니다.

**환경별 로깅 전략:**

```xml
<!-- local 환경: 개발용 -->
<springProfile name="local">
    <logger name="whispy_server.whispy" level="DEBUG"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
</springProfile>

<!-- test 환경: 통합 테스트용 -->
<springProfile name="test">
    <logger name="whispy_server.whispy" level="DEBUG"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
</springProfile>

<!-- prod/stag 환경: 프로덕션용 -->
<springProfile name="prod,stag">
    <logger name="whispy_server.whispy" level="DEBUG"/>
    <logger name="org.hibernate.SQL" level="WARN"/>  <!-- SQL 쿼리 최소화 -->
    <appender-ref ref="FILE"/>        <!-- /opt/whispy/logs/whispy.log -->
    <appender-ref ref="ERROR_FILE"/>  <!-- /opt/whispy/logs/whispy-error.log -->
</springProfile>
```

**주의사항:**
- `application.yml`의 `jpa.show-sql`은 각 환경별 yml에서 관리
- 로깅 레벨은 `logback-spring.xml`에서만 정의 (application.yml에 중복 정의 금지)
- 프로덕션 로그 파일: 90일 보관, 일반 로그 최대 10GB, 에러 로그 최대 3GB

## 데이터베이스 마이그레이션 (Flyway)

DB 스키마 변경이 필요하면 **반드시 Flyway 마이그레이션 파일을 생성**해야 합니다.

### 마이그레이션 파일 생성 규칙

**위치:** `src/main/resources/db/migration/`

**네이밍 컨벤션:** `V{버전}__{설명}.sql`
- 버전은 순차적으로 증가 (V1, V2, V3...)
- 언더스코어 2개(`__`)로 버전과 설명 구분
- 설명은 영문으로 작성 (snake_case 또는 PascalCase)

**예시:**
```
V1__Add_video_url_to_music.sql
V2__rename_batch_tables_to_upper.sql
V3__rename_to_default_batch.sql
V4__change_admin_id_to_bigint.sql
V5__Add_artist_and_description_to_music.sql
```

### 마이그레이션 파일 작성 예시

```sql
-- V7__Add_index_to_focus_session.sql
ALTER TABLE tbl_focus_session
    ADD INDEX idx_user_started (user_id, started_at);

-- V8__Add_fcm_token_column.sql
ALTER TABLE tbl_user
    ADD COLUMN fcm_token VARCHAR(255) NULL COMMENT 'FCM 토큰';
```

### 중요 규칙

1. **절대 수정 금지**: 이미 적용된 마이그레이션 파일은 절대 수정하지 않음
2. **롤백 불가**: Flyway는 자동 롤백을 지원하지 않음 (실패 시 수동 복구 필요)
3. **순차 실행**: 버전 순서대로 실행되므로 누락 없이 순차 증가
4. **테스트 환경**: 통합 테스트에서는 Flyway 비활성화 (`application-test.yml`에서 `flyway.enabled: false`)

### Flyway 설정

```yaml
# application.yml
spring:
  flyway:
    enabled: true                      # 프로덕션/개발 환경에서 활성화
    baseline-on-migrate: true          # 기존 DB에 Flyway 적용
    baseline-version: 0                # 베이스라인 버전
    locations: classpath:db/migration  # 마이그레이션 파일 위치
    validate-on-migrate: false         # 마이그레이션 검증 비활성화
```

### JPA DDL 설정과의 관계

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Flyway와 함께 사용 시 validate 권장
```

**설정값:**
- `validate`: 엔티티와 DB 스키마가 일치하는지만 검증 (프로덕션 권장)
- `create-drop`: 테스트 환경에서만 사용 (Flyway 비활성화 필요)
- `update`, `create`: 절대 사용 금지 (Flyway와 충돌)

### 마이그레이션 파일 생성 체크리스트

새로운 엔티티 추가 시:
- [ ] JPA 엔티티 생성
- [ ] Flyway 마이그레이션 파일 생성 (`CREATE TABLE`)
- [ ] 인덱스 추가 마이그레이션 (필요 시)
- [ ] `ddl-auto: validate` 확인

기존 엔티티 수정 시:
- [ ] JPA 엔티티 수정
- [ ] Flyway 마이그레이션 파일 생성 (`ALTER TABLE`)
- [ ] 로컬에서 마이그레이션 테스트

## 주요 기술 스택

- Java 21 (Virtual Threads 활성화)
- Spring Boot 3.4.5, Spring Security, Spring Batch
- MySQL + JPA/Hibernate + QueryDSL 5.0.0
- Redis (Refresh Token, 캐시)
- Cloudflare R2 (S3 호환 파일 저장소)
- Flyway (DB 마이그레이션)
- MapStruct 1.6.0 (Entity ↔ Domain 매핑)
- Firebase Admin SDK (FCM)
- Sentry + Prometheus (모니터링)

## 파일 저장소 구조

- 업로드/삭제/조회는 로컬 파일시스템이 아니라 `FileStoragePort` 를 통해 추상화합니다.
- 운영 구현체는 `domain/file/adapter/out/external/R2FileStorageAdapter` 이며, `global/config/r2/R2Config` 에서 S3Client 를 구성합니다.
- 업로드 응답 URL 계약은 기존과 동일하게 `BASE_URL + "/file/{folder}/{fileName}"` 형식을 유지합니다.
- 공개 파일 조회는 `FileAssetController` -> `FileAssetResponseAssembler` -> `FileReadService` -> `FileStoragePort` 흐름으로 처리합니다.
- `/file/**` 는 더 이상 정적 리소스 매핑이 아니라 애플리케이션이 직접 받아 R2 객체를 읽어 응답합니다.
- 관련 설정은 `spring.whispy.file.base-url`, `spring.whispy.r2.account-id`, `spring.whispy.r2.access-key-id`, `spring.whispy.r2.secret-access-key`, `spring.whispy.r2.bucket` 를 사용합니다.

## 새로운 도메인 기능 추가 순서

1. `model/`에 `@Aggregate` Record 도메인 모델 생성 (Compact Constructor에서 검증)
2. `application/port/in/`에 UseCase 인터페이스 정의
3. `application/port/out/`에 Port 인터페이스 정의
4. `application/service/`에 Service 구현
5. `adapter/in/web/`에 Controller, Request/Response DTO 생성
6. `adapter/out/entity/`에 JPA 엔티티 생성 (BaseTimeEntity 상속)
7. `adapter/out/mapper/`에 MapStruct 매퍼 생성
8. `adapter/out/persistence/`에 Repository 및 Adapter 구현

## 필수 규칙

- **Service 생성 시**: 반드시 단위 테스트 작성 필수 (`src/test/java/.../domain/{도메인}/application/service/unit/`)
- **Controller 생성/수정 시**: 반드시 ApiDocument 인터페이스 구현 및 수정 필수 (`global/document/api/{도메인}/`)
- **새 예외 추가 시**: ErrorCode enum에 코드 추가 후 예외 클래스 생성

## 네이밍 컨벤션

- **UseCase**: `{동작}{엔티티}UseCase` (예: `SaveFocusSessionUseCase`)
- **Service**: `{동작}{엔티티}Service` (예: `SaveFocusSessionService`)
- **Controller**: `{엔티티}Controller`
- **Port**: `{동작}{엔티티}Port` 또는 `Query{엔티티}Port`
- **Adapter**: `{엔티티}PersistenceAdapter`

## 테스트 패턴

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("서비스명 테스트")
class ServiceTest {
    @InjectMocks private TargetService service;
    @Mock private SomeDependency dependency;

    @Test
    @DisplayName("시나리오 설명")
    void whenCondition_thenExpectedResult() {
        // given-when-then
    }
}
```

## Git 커밋 컨벤션

### 포맷
```
<타입> : <설명>
<타입> ( #이슈번호 ) : <설명>
```

### 타입
- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **refactor**: 코드 리팩토링
- **chore**: 빌드, 설정 파일 수정
- **docs**: 문서 수정
- **test**: 테스트 코드 추가/수정
- **perf**: 성능 개선

### 예시
```
feat ( #72 ) : 인덱스 추가
fix : Rate Limit과 코드 만료 시간의 불일치 해결
```

### 규칙
- 타입과 콜론(:) 사이에 공백
- 이슈 번호: `( #번호 )` 형식 (괄호 안쪽 공백)
- 설명은 한글로 작성
- 커밋은 사용자가 요청할 때 한다.
- 푸쉬는 사용자가 요청하지 않으면 절대 하지 않는다.

---

## 논리적 외래키 (Logical Foreign Key) 규칙

### 핵심 원칙

이 프로젝트는 **JPA의 물리적 FK를 사용하지 않고 논리적 FK만 사용**합니다.

**금지 사항:**
- ❌ `@ManyToOne`, `@OneToMany`, `@OneToOne`, `@ManyToMany` 사용 금지
- ❌ 엔티티 간 직접 참조 금지

**허용 사항:**
- ✅ `private Long userId;` (논리적 FK)
- ✅ `private String email;` (논리적 FK)
- ✅ `@ElementCollection` + `@CollectionTable` (단순 컬렉션 매핑용)

### 참조 관계

#### User 참조
```java
// ✅ userId (Long) 참조 - 6개 엔티티
FocusSession, SleepSession, MeditationSession,
MusicLike, ListeningHistory, SoundSpaceMusic

// ✅ email (String) 참조 - 3개 엔티티
Notification, TopicSubscription, Subscription
```

#### Music 참조
```java
// ✅ musicId (Long) 참조 - 3개 엔티티
MusicLike, ListeningHistory, SoundSpaceMusic
```

### CASCADE 삭제 구현

논리적 FK를 사용하므로 **CASCADE 삭제는 서비스 레이어에서 명시적으로 구현**해야 합니다.

#### User 삭제 시 (UserWithdrawalService)
```java
@Transactional  // 필수!
public void withdrawal() {
    User user = userFacadeUseCase.currentUser();
    Long userId = user.id();
    String email = user.email();

    // 1️⃣ 자식 데이터 먼저 삭제 (userId 기반)
    deleteFocusSessionPort.deleteByUserId(userId);
    deleteSleepSessionPort.deleteByUserId(userId);
    deleteMeditationSessionPort.deleteByUserId(userId);
    deleteMusicLikePort.deleteByUserId(userId);
    deleteListeningHistoryPort.deleteByUserId(userId);
    deleteSoundSpaceMusicPort.deleteByUserId(userId);

    // 2️⃣ 자식 데이터 삭제 (email 기반)
    deleteNotificationPort.deleteByEmail(email);
    deleteTopicSubscriptionPort.deleteByEmail(email);

    // 3️⃣ 부모 데이터 마지막에 삭제
    refreshTokenRepository.deleteById(userId);
    userDeletePort.deleteById(userId);
}
```

#### Music 삭제 시 (DeleteMusicService)
```java
@Transactional
public void execute(Long id) {
    // 1️⃣ 자식 데이터 먼저 삭제
    deleteMusicLikePort.deleteAllByMusicId(id);
    deleteListeningHistoryPort.deleteAllByMusicId(id);
    deleteSoundSpaceMusicPort.deleteAllByMusicId(id);

    // 2️⃣ 부모 데이터 마지막에 삭제
    musicDeletePort.deleteById(id);
}
```

### 필수 규칙

1. **인덱스 필수 설정**
```java
@Table(name = "tbl_focus_session", indexes = {
    @Index(name = "idx_user_started", columnList = "user_id, started_at")
})
```

2. **Unique Constraint 설정** (중복 방지가 필요한 경우)
```java
@Table(name = "tbl_music_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "music_id"})
    })
```

3. **트랜잭션 필수**
- CASCADE 삭제는 반드시 `@Transactional` 안에서 실행

4. **삭제 순서 준수**
- 자식 데이터 먼저 삭제 → 부모 데이터 나중에 삭제

### 새 엔티티 추가 시 체크리스트

User나 Music을 참조하는 엔티티를 추가할 때:

1. [ ] 논리적 FK 필드 추가 (`userId`, `email`, `musicId`)
2. [ ] 인덱스 설정 (`@Index`)
3. [ ] Unique 제약 필요 시 설정
4. [ ] Delete Port 인터페이스 생성
5. [ ] `deleteByUserId` 또는 `deleteByMusicId` 메서드 추가
6. [ ] **UserWithdrawalService** 또는 **DeleteMusicService**에 CASCADE 삭제 로직 추가
7. [ ] 단위 테스트 작성

### 데이터 정합성 보장

논리적 FK는 DB 제약조건이 없지만, 다음 방법으로 무결성을 보장합니다:

1. **애플리케이션 레벨 검증**
   - Service에서 존재 여부 확인 후 저장
   - 예: `if (!userExists(userId)) throw UserNotFoundException.EXCEPTION;`

2. **트랜잭션 원자성**
   - CASCADE 삭제가 모두 성공하거나 모두 롤백

3. **테스트 코드**
   - 모든 CASCADE 삭제 로직에 대한 단위 테스트 작성
   - 예: `UserWithdrawalServiceTest`에서 모든 삭제 검증

### 장점

- ✅ N+1 쿼리 문제 원천 차단
- ✅ 도메인 간 결합도 최소화 (Hexagonal Architecture 적합)
- ✅ 순환 참조 불가능
- ✅ 독립적인 도메인 테스트 가능

### 단점 및 대응

- ⚠️ CASCADE 삭제 수동 구현 필요 → Delete Port 패턴으로 체계화
- ⚠️ 참조 무결성 DB 미보장 → 애플리케이션 검증 + Unique Constraint
