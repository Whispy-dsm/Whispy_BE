# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

Whispy_BE는 수면, 집중, 명상 애플리케이션을 위한 Spring Boot 백엔드 서비스입니다. Java 21을 사용하며, DDD 개념을 적용한 헥사고날 아키텍처(Ports & Adapters) 패턴으로 구현되었습니다.

## 개발 명령어

### 빌드 및 실행
```bash
./gradlew build          # 프로젝트 빌드
./gradlew clean build    # 클린 후 빌드
./gradlew bootRun        # 애플리케이션 실행
./gradlew bootJar        # 실행 가능한 JAR 생성
```

### 테스트
```bash
./gradlew test           # 모든 테스트 실행
./gradlew test --tests ClassName  # 특정 테스트 클래스 실행
./gradlew test --tests ClassName.testMethodName  # 단일 테스트 메서드 실행
```

### 기타 Gradle 명령어
```bash
./gradlew compileJava    # Java 소스 컴파일
./gradlew dependencies   # 프로젝트 의존성 확인
```

## 아키텍처

### 헥사고날 아키텍처 (Ports & Adapters)

이 코드베이스는 도메인 로직과 인프라를 명확히 분리하는 헥사고날 아키텍처를 따릅니다:

**도메인별 패키지 구조:**
```
domain/{도메인명}/
├── adapter/
│   ├── in/web/          # REST 컨트롤러 (인바운드 어댑터)
│   └── out/
│       ├── persistence/ # 데이터베이스 어댑터 (아웃바운드)
│       ├── external/    # 외부 API 클라이언트 (아웃바운드)
│       └── mapper/      # Entity ↔ Domain 매퍼
├── application/
│   ├── port/
│   │   ├── in/          # 유스케이스 인터페이스 (인바운드 포트)
│   │   └── out/         # 레포지토리/외부 인터페이스 (아웃바운드 포트)
│   └── service/         # 유스케이스 구현체
├── facade/              # 횡단 관심사를 위한 파사드 패턴
└── model/               # 도메인 모델 (애그리게잇)
```

**핵심 패턴:**
- **애그리게잇(Aggregates)**: `@Aggregate` 어노테이션으로 표시된 도메인 모델 (예: User, Music, SleepSession)
- **유스케이스(Use Cases)**: `@UseCase` 어노테이션으로 표시된 애플리케이션 작업을 나타내는 서비스 인터페이스
- **포트(Ports)**: `port/in` (주도하는 포트)와 `port/out` (주도되는 포트)의 인터페이스
- **어댑터(Adapters)**: `adapter/` 패키지의 구현체
  - `in/web` - REST 컨트롤러
  - `out/persistence` - JPA 레포지토리
  - `out/external` - 외부 API 클라이언트 (Kakao, Google Play 등)

### 도메인 모듈

주요 도메인:
- **user** - 사용자 관리, 인증, OAuth
- **auth** - 이메일 인증
- **admin** - 관리자 기능
- **music** - 음악 카탈로그 및 관리
- **sleepsession** - 수면 세션 추적
- **focussession** - 집중/생산성 세션
- **meditationsession** - 명상 세션 추적
- **statistics** - 수면, 집중, 활동 통계
- **payment** - Google Play 구독 관리
- **notification** - FCM 푸시 알림
- **like** - 음악 좋아요/즐겨찾기
- **soundspace** - 사용자 커스텀 음악 컬렉션
- **history** - 청취 히스토리
- **announcement** - 시스템 공지사항
- **topic** - FCM 토픽 구독

### Global 인프라

`global/` 패키지에 위치:
- **security**: JWT 인증, OAuth2 (Google), 커스텀 UserDetailsService
- **oauth**: OAuth 핸들러 및 파서 (Google, Kakao)
- **feign**: 외부 API 클라이언트 (Google Play, Discord, Kakao) 및 커스텀 에러 처리
- **config**: Spring 설정 (Security, JPA, QueryDSL, Feign, Redis 등)
- **exception**: Discord 웹훅을 통한 전역 예외 처리
- **file**: 이미지 리사이징(WebP 지원)을 포함한 파일 업로드/검증

## 기술 스택

- **프레임워크**: Spring Boot 3.4.5
- **Java**: 21 (가상 스레드 활성화)
- **데이터베이스**: MySQL with JPA/Hibernate
- **캐시**: Redis
- **보안**: Spring Security with JWT (io.jsonwebtoken)
- **OAuth**: Spring OAuth2 Client (Google, Kakao)
- **API 문서**: SpringDoc OpenAPI (Swagger)
- **모니터링**: Sentry, Prometheus, Spring Actuator
- **외부 API**: Google Play, FCM, Kakao, Discord 웹훅을 위한 Feign 클라이언트
- **쿼리**: QueryDSL 5.0.0
- **매핑**: MapStruct 1.6.0
- **이미지 처리**: Thumbnailator, WebP ImageIO
- **이메일**: Spring Mail (Gmail SMTP)
- **알림**: Firebase Admin SDK for FCM
- **배치**: Spring Batch

## 설정

애플리케이션은 환경 변수로 설정됩니다 (`application.yml` 참고):
- 데이터베이스: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- Redis: `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`
- JWT: `JWT_HEADER`, `JWT_PREFIX`, `JWT_SECRET`
- OAuth: `GOOGLE_CLIENT`, `GOOGLE_SECRET`
- 파일 저장소: `UPLOAD_PATH`, `BASE_URL`
- 외부 서비스: Discord 웹훅, Sentry DSN, FCM 자격증명
- 이메일: `MAIL_USERNAME`, `MAIL_PASSWORD`

**중요**: `jpa.hibernate.ddl-auto`가 `create`로 설정되어 있음 - 프로덕션에서는 `validate` 또는 `none`으로 변경 필요.

## 보안

### 인증 플로우
1. **JWT 기반**: 인증된 엔드포인트에 대해 커스텀 JwtTokenFilter가 토큰 검증
2. **OAuth2**: 커스텀 성공/실패 핸들러를 사용한 Google OAuth2
3. **Kakao OAuth**: Feign 클라이언트를 통한 수동 구현 (KakaoUserInfoAdapter)
4. **역할 기반**: `/admin/**` 엔드포인트는 ADMIN 역할 필요

### 공개 엔드포인트
- 사용자 가입/로그인/토큰 재발급
- OAuth 콜백
- 파일 제공 (프로필 이미지, 음악 파일)
- 이메일 인증
- Swagger UI
- 구매 검증 웹훅
- Actuator 엔드포인트

## 에러 처리

- **GlobalExceptionHandler**: 도메인 예외를 잡아서 구조화된 ErrorResponse 반환
- **GlobalExceptionFilter**: 필터 레벨 예외 처리
- **Discord 연동**: DiscordNotificationService를 통해 Discord 웹훅으로 에러 전송
- **Sentry**: Sentry에서 애플리케이션 에러 추적

## 외부 연동

### Google Play 결제
- 구독 검증을 위한 GooglePlayFeignClient
- 서비스 계정 인증을 위한 GooglePlayAccessTokenProvider
- `/webhook/google-play`에서 구매 알림 웹훅 엔드포인트
- 구독 상태 관리 (ProductType, SubscriptionState)

### Firebase Cloud Messaging (FCM)
- Firebase Admin SDK를 통한 FCM 알림
- 토픽 기반 메시징
- 디바이스 토큰 관리

### 파일 업로드
- 최대 파일 크기: 50MB
- Thumbnailator를 사용한 이미지 리사이징
- WebP 인코딩 지원
- 광범위한 파일 검증 (확장자, MIME 타입, 파일명 sanitization)

## 일반적인 작업 플로우

### 새로운 도메인 기능 추가하기

1. `domain/{이름}/model`에 `@Aggregate`를 사용한 도메인 모델 생성
2. `application/port/in`에 `@UseCase`를 사용한 유스케이스 인터페이스 정의
3. `application/port/out`에 레포지토리 포트 정의
4. `application/service`에 서비스 구현
5. `adapter/in/web`에 REST 컨트롤러 생성
6. `adapter/out/persistence`에 영속성 어댑터 구현
7. `adapter/out/persistence`에 엔티티와 매퍼 생성

### 통합 테스트 추가하기

테스트는 Mockito와 함께 JUnit 5를 사용합니다. 포괄적인 단위 테스트 예제는 `GetSleepStatisticsServiceTest` 참고:
- `@ExtendWith(MockitoExtension.class)`
- 의존성을 위한 `@Mock`
- 테스트 대상 서비스를 위한 `@InjectMocks`
- `@ParameterizedTest`와 `@MethodSource`를 사용한 파라미터화된 테스트

## 네이밍 컨벤션

- **Use Cases**: `{동작}{엔티티}UseCase` (예: `SaveSleepSessionUseCase`)
- **Services**: `{동작}{엔티티}Service` (예: `SaveSleepSessionService`)
- **Controllers**: `{엔티티}Controller` (예: `SleepSessionController`)
- **Ports**: `{동작}{엔티티}Port` 또는 `Query{엔티티}Port` (예: `QueryUserPort`)
- **Adapters**: `{엔티티}PersistenceAdapter` (예: `UserPersistenceAdapter`)
- **Facades**: 횡단 관심사를 위한 `{엔티티}Facade`

## Git 커밋 컨벤션

이 프로젝트는 다음과 같은 커밋 메시지 규칙을 따릅니다:

### 기본 포맷
```
<타입> : <설명>
<타입> ( #이슈번호 ) : <설명>
```

### 커밋 타입
- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **refactor**: 코드 리팩토링 (기능 변경 없음)
- **chore**: 빌드, 설정 파일 수정
- **docs**: 문서 수정
- **test**: 테스트 코드 추가/수정
- **style**: 코드 포맷팅, 세미콜론 누락 등
- **perf**: 성능 개선

### 커밋 메시지 예시
```bash
feat : dto에 schema 추가
feat ( #72 ) : 인덱스 추가
fix ( #72 ) : 백틱으로 감싸 안정성 강화
refactor : 코드 리팩토링
refactor ( #67 ) : 이벤트 수신 동의 여부에 따라 topic 구독이 달라지도록 수정
fix : Rate Limit과 코드 만료 시간의 불일치 + 예외 처리 버그 해결
```

### 규칙
- 타입과 콜론(:) 사이에 공백 있음
- 이슈 번호가 있는 경우 `( #번호 )` 형식으로 표시 (괄호 안쪽에 공백 있음)
- 설명은 한글로 작성하며, 무엇을 했는지 명확하게 기술
- 설명은 "~하도록 수정", "~추가", "~제거" 등의 서술형으로 작성
