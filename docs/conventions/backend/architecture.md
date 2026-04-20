# Backend Architecture Convention

## 프로젝트 개요

Whispy_BE는 수면, 집중, 명상 웰니스 애플리케이션을 위한 Spring Boot 백엔드입니다.
Java 21과 Spring Boot 3.4.5 기반이며, 헥사고날 아키텍처(Ports & Adapters)를 적용합니다.

## 주요 기술 스택

- Java 21 with Virtual Threads
- Spring Boot 3.4.5, Spring Security, Spring Batch
- MySQL, JPA/Hibernate, QueryDSL 5.0.0
- Redis for refresh token and cache
- Cloudflare R2 as S3-compatible file storage
- Flyway for database migration
- MapStruct 1.6.0 for entity-domain mapping
- Firebase Admin SDK for FCM
- Sentry and Prometheus for monitoring

## 헥사고날 아키텍처

도메인별 패키지는 아래 구조를 따릅니다.

```text
domain/{도메인명}/
├── adapter/
│   ├── in/web/              # REST 컨트롤러, Request/Response DTO
│   └── out/
│       ├── entity/          # JPA 엔티티
│       ├── mapper/          # MapStruct 매퍼
│       ├── persistence/     # Repository 어댑터
│       └── external/        # 외부 API 클라이언트
├── application/
│   ├── port/
│   │   ├── in/              # UseCase 인터페이스
│   │   └── out/             # Port 인터페이스
│   └── service/             # UseCase 구현체
├── facade/                  # 횡단 관심사를 위한 파사드
└── model/                   # @Aggregate, record 기반 도메인 모델
    └── types/               # 도메인별 enum
```

핵심 규칙:

- 도메인 모델은 `@Aggregate`로 표시합니다.
- 도메인 모델은 record 기반 불변 객체를 우선합니다.
- 도메인 규칙은 record compact constructor에서 검증합니다.
- UseCase 인터페이스는 기능별로 명시적으로 정의합니다.
- Port와 Adapter로 의존성 역전을 유지하고 도메인을 격리합니다.
- 비즈니스 규칙은 Controller나 JPA entity보다 domain model과 application service에 둡니다.
- Controller는 진입점 역할만 하고, service는 use case 흐름과 트랜잭션을 책임집니다.
- Persistence adapter는 port 구현체로만 존재합니다.
- 외부 시스템 연동은 `adapter/out/external`로 분리합니다.

## 도메인 모델 예시

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

## 새로운 도메인 기능 추가 순서

1. `model/`에 `@Aggregate` record 도메인 모델을 생성합니다.
2. `application/port/in/`에 UseCase 인터페이스를 정의합니다.
3. `application/port/out/`에 Port 인터페이스를 정의합니다.
4. `application/service/`에 Service 구현체를 작성합니다.
5. `adapter/in/web/`에 Controller와 Request/Response DTO를 생성합니다.
6. `adapter/out/entity/`에 BaseTimeEntity를 상속한 JPA 엔티티를 생성합니다.
7. `adapter/out/mapper/`에 MapStruct 매퍼를 생성합니다.
8. `adapter/out/persistence/`에 Repository와 Adapter 구현체를 작성합니다.

## 파일 저장소 구조

- 업로드, 삭제, 조회는 로컬 파일시스템이 아니라 `FileStoragePort`로 추상화합니다.
- 운영 구현체는 `domain/file/adapter/out/external/R2FileStorageAdapter`입니다.
- `global/config/r2/R2Config`에서 S3Client를 구성합니다.
- 업로드 응답 URL 계약은 `BASE_URL + "/file/{folder}/{fileName}"` 형식을 유지합니다.
- 공개 파일 조회는 `FileAssetController` -> `FileAssetResponseAssembler` -> `FileReadService` -> `FileStoragePort` 흐름으로 처리합니다.
- `/file/**`는 정적 리소스 매핑이 아니라 애플리케이션이 직접 받아 R2 객체를 읽어 응답합니다.
- 대용량 파일은 스트림 방식으로 처리합니다.
- 오디오 파일은 Range 요청을 고려하고 기존 공개 URL 계약을 깨지 않습니다.
- 관련 설정은 `spring.whispy.file.base-url`, `spring.whispy.r2.account-id`, `spring.whispy.r2.access-key-id`, `spring.whispy.r2.secret-access-key`, `spring.whispy.r2.bucket`를 사용합니다.
