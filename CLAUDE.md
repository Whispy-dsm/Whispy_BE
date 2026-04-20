# CLAUDE.md

This file provides guidance to coding agents when working in this repository.

## 프로젝트 개요

Whispy_BE는 수면, 집중, 명상 웰니스 애플리케이션을 위한 Spring Boot 백엔드입니다.
Java 21과 Spring Boot 3.4.5 기반이며, 헥사고날 아키텍처(Ports & Adapters)를 적용합니다.

## 백엔드 컨벤션 참조

Notion `컨벤션/백엔드` 페이지와 로컬 [Backend Convention Index](docs/conventions/backend/README.md)가 이 프로젝트의 백엔드 작업 기준입니다.
작업을 시작할 때는 인덱스를 먼저 읽고, 작업 주제에 맞는 문서를 추가로 읽은 뒤 해당 컨벤션을 따릅니다.

주제별 컨벤션:

- [Workflow](docs/conventions/backend/workflow.md)
- [Architecture](docs/conventions/backend/architecture.md)
- [Implementation Patterns](docs/conventions/backend/implementation-patterns.md)
- [Database](docs/conventions/backend/database.md)
- [Auth and FCM](docs/conventions/backend/auth-fcm.md)
- [Testing](docs/conventions/backend/testing.md)
- [Git](docs/conventions/backend/git.md)

Notion과 로컬 문서가 함께 갱신되어야 하는 변경이라면, 로컬 문서도 같은 범위로 수정합니다.
이 세션에서 Notion에 접근할 수 없으면 로컬 문서를 기준으로 작업합니다.

## 빠른 명령어

Linux 또는 macOS:

```bash
./gradlew build
./gradlew test
./gradlew test --tests ClassName
./gradlew test --tests ClassName.testMethodName
./gradlew bootRun
```

Windows:

```bash
gradlew.bat build
gradlew.bat test
gradlew.bat test --tests ClassName
```

## 최상위 필수 규칙

- 분석, 리뷰, 성능, 아키텍처, 보안, 리팩토링 제안 작업은 [Workflow](docs/conventions/backend/workflow.md)의 분석 우선 흐름을 따른다.
- Service 생성 시 단위 테스트를 작성한다.
- Controller 생성 또는 수정 시 `global/document/api/{도메인}/`의 ApiDocument 인터페이스도 함께 관리한다.
- DB 스키마 변경 시 Flyway 마이그레이션 파일을 생성한다.
- JPA 물리적 FK 관계 매핑을 사용하지 않고 논리적 FK 규칙을 따른다.
- 클래스, 인터페이스, enum, record, 생성자, 메서드에는 Javadoc을 작성한다.
- 커밋은 사용자가 요청할 때만 하고, 푸시는 사용자가 요청하지 않으면 하지 않는다.
