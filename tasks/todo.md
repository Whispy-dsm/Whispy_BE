## 요구사항 요약
- 로컬 파일 저장을 제거하고 Cloudflare R2만 사용한다.
- 업로드/삭제 API 계약은 유지한다.
- 클라이언트와 DB에 저장되는 파일 URL은 기존 `https://whispy.whispy-company.site/file/{folder}/{filename}` 형태를 유지한다.
- `/file/**` 요청은 애플리케이션이 직접 받아 R2에서 파일을 읽어 응답한다.

## 구현 계획
- [ ] File storage 포트/어댑터 구조 도입
- [ ] R2 설정 및 SDK 연동 추가
- [ ] `/file/**` 앱 직접 처리 경로로 전환
- [ ] 파일 도메인 서비스/테스트 갱신
- [ ] 진단, 테스트, 빌드 검증 완료

## 수정할 파일
- `build.gradle`: AWS SDK v2 S3 의존성 추가
- `src/main/java/whispy_server/whispy/domain/file/**`: 저장소 추상화, 서비스/컨트롤러/조회 로직 추가
- `src/main/java/whispy_server/whispy/global/config/**`: R2 설정, 웹/보안 설정 조정
- `src/main/java/whispy_server/whispy/global/file/**`: 파일/R2 프로퍼티 갱신
- `src/test/java/whispy_server/whispy/domain/file/**`: 단위 테스트 갱신/추가

## 예상 위험 요소
- `/file/**` 정적 매핑 제거 후 기존 공개 URL이 깨질 수 있음
- R2 업로드 시 SDK 설정 오류로 서명 실패가 발생할 수 있음
- 폴더별 파일 검증/압축 규칙이 누락되면 운영 데이터가 깨질 수 있음

## Review
- `./gradlew.bat test --tests whispy_server.whispy.domain.file.application.service.unit.FileUploadServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileDeleteServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileReadServiceTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest` 통과
- `./gradlew.bat build` 통과
- LSP diagnostics는 Java 서버 초기화 타임아웃으로 결과를 회수하지 못했지만, Gradle 컴파일/전체 빌드는 성공
- 리뷰 피드백 반영:
  - `R2FileStorageAdapter`를 `adapter/out/external`로 이동
  - test-only 저장소 어댑터를 `src/test/java`로 이동
  - `FileStoragePort.upload(...)`를 단일 메서드로 단순화
  - `ImageFolder` 내부 경로 매핑 로직 제거 후 `ImageFolderPathResolver`로 분리
  - 사용되지 않는 `uploadPath` / `upload-path` 설정 제거

## 2026-03-30 Skill Metadata Fix
- [x] 문제 재현 및 원인 확인
- [x] `java-coding-standards/SKILL.md` frontmatter 수정
- [x] `springboot-verification/SKILL.md` frontmatter 수정
- [x] 수정 결과 검증 및 원인 정리

### Review
- 원인: YAML frontmatter의 `description` 값 안에 따옴표 없는 `:`가 있어 메타데이터 파싱 실패
- 수정: 두 `SKILL.md`의 `description` 값을 큰따옴표로 감쌈
- 검증: Node `yaml` 패키지로 두 파일 frontmatter 파싱 성공 확인

## 2026-03-30 Review Follow-up
- [x] R2 삭제 시 누락 파일 404 계약 복구
- [x] 삭제 회귀를 잡는 저장소 단위 테스트 추가
- [x] `/file/**` 공개 접근 경로 통합 테스트 추가
- [x] 관련 테스트 재실행 및 결과 기록

### Review
- 수정: `R2FileStorageAdapter.delete(...)`에서 삭제 전 `headObject`로 존재 여부를 확인해 누락 파일은 `FileNotFoundException`으로 매핑
- 추가 테스트:
  - `R2FileStorageAdapterTest`: 누락 파일 삭제 시 404 계약 회귀 테스트
  - `FileAssetControllerIntegrationTest`: 인증 없이 `/file/**` 공개 URL 접근 및 누락 파일 404 응답 검증
- 검증:
  - `./gradlew.bat test --tests whispy_server.whispy.domain.file.adapter.out.external.R2FileStorageAdapterTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.integration.FileAssetControllerIntegrationTest --tests whispy_server.whispy.domain.file.application.service.unit.FileUploadServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileDeleteServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileReadServiceTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest` 통과

## 2026-03-30 R2 Backfill Plan
- [ ] 운영 서버에 `awscli` 설치
- [ ] R2 credentials/env 준비
- [ ] `/opt/whispy/uploads` 기준 dry-run sync 실행
- [ ] 실제 sync 1차 실행
- [ ] 앱 전환 직후 sync 2차 실행
- [ ] 샘플 URL 및 개수 검증
- [ ] 로컬 파일 보관 후 정리 시점 결정

### Premises
- 기존 공개 URL 형식은 유지하고 DB는 수정하지 않는다.
- R2 object key는 `/opt/whispy/uploads` 하위 상대경로와 동일해야 한다.
- 마이그레이션은 `aws s3 sync` 방식으로 수행하고, 기존 로컬 파일은 즉시 삭제하지 않는다.

### Approaches Considered
- A. 서버에 `awscli` 설치 후 `/opt/whispy/uploads`를 직접 R2로 sync
- B. 설치 없이 Python 스크립트로 수동 업로드
- C. 앱 내부에 일회성 backfill runner 추가 후 배포

### Recommended Approach
- A를 선택한다. 현재 서버 파일은 179개 / 345MB로 작고, Ubuntu 24.04 서버에서 바로 실행 가능하며 운영 절차가 가장 단순하다.

## 2026-03-30 FileAsset Refactor
- [x] `FileAssetResponseAssembler` 테스트 추가
- [x] `FileAssetController`를 assembler 위임-only 구조로 변경
- [x] 기존 공개 파일 조회 테스트 갱신
- [x] 관련 파일 테스트 재실행 및 결과 기록

### Review
- 구조 변경: `FileAssetController`는 `FileAssetResponseAssembler` 호출만 수행하도록 단순화
- 로직 이동: media type 결정, cache-control/content-length 설정, `ResponseEntity` 조립을 assembler로 이동
- 테스트 추가/갱신:
  - `FileAssetResponseAssemblerTest`
  - `FileAssetControllerTest`
  - `FileAssetControllerIntegrationTest`
- 검증:
  - `./gradlew.bat test --tests whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssemblerTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.unit.FileAssetControllerTest --tests whispy_server.whispy.domain.file.adapter.in.web.controller.integration.FileAssetControllerIntegrationTest --tests whispy_server.whispy.domain.file.application.service.unit.FileReadServiceTest` 통과
  - 참고: 테스트 종료 시 Spring Batch `userItemReader` close warning이 로그에 남았지만 빌드는 성공

## 2026-03-30 File Resolver Cleanup
- [x] 사용되지 않는 `ImageFolderPathResolver.toObjectKey(String, String)` 제거
- [x] 관련 파일 도메인 테스트 재실행 및 결과 기록

### Review
- 정리: 호출처가 없는 string 기반 object key 오버로드 제거
- 검증:
  - `./gradlew.bat test --tests whispy_server.whispy.domain.file.application.service.unit.FileUploadServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileDeleteServiceTest --tests whispy_server.whispy.domain.file.application.service.unit.FileReadServiceTest --tests whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssemblerTest` 통과
