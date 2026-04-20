# Backend Convention Index

이 디렉터리는 Notion `컨벤션/백엔드` 페이지를 로컬에서 참조하기 위한 백엔드 컨벤션 문서 모음입니다.
현재 문서는 기존 `AGENTS.md`에 정의되어 있던 백엔드 규칙을 주제별로 분리한 기준입니다.

## 읽는 순서

작업을 시작할 때는 이 인덱스를 먼저 열고, 작업 범위에 맞는 문서를 함께 읽은 뒤 해당 컨벤션을 따릅니다.

1. [Workflow](workflow.md): 분석, 승인, 편집, 검증 흐름
2. [Architecture](architecture.md): 헥사고날 아키텍처, 기술 스택, 파일 저장소, 도메인 추가 순서
3. [Implementation Patterns](implementation-patterns.md): Controller, DTO, 예외, API 문서화, 로깅, Javadoc, 네이밍
4. [Database](database.md): Flyway, JPA DDL, 논리적 외래키, cascade 삭제
5. [Auth and FCM](auth-fcm.md): 로그인 토큰, FCM 토큰, 토픽 재등록
6. [Testing](testing.md): 테스트 필수 규칙과 단위 테스트 패턴
7. [Git](git.md): 브랜치, 커밋 메시지, gitmoji, 커밋/푸시 제한

## 동기화 규칙

- Notion `컨벤션/백엔드` 페이지가 최신 기준이면 이 로컬 문서도 같은 변경을 반영합니다.
- 로컬 작업 중 Notion에 접근할 수 없으면 이 디렉터리의 문서를 기준으로 작업합니다.
- `AGENTS.md`에는 전체 컨벤션을 반복해서 적지 않고, 이 인덱스와 주제별 문서를 참조하도록 유지합니다.
