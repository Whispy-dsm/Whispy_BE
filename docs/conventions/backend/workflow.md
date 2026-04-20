# Backend Workflow Convention

## 분석 우선, 편집은 나중에

분석이 필요한 작업에서는 코드 편집 전에 관련 파일을 먼저 읽고, 발견 내용을 문서화합니다.

적용 대상:

- 코드 리뷰
- 데이터베이스 인덱스 분석
- 성능 최적화
- 아키텍처 검토
- 보안 감사
- 리팩토링 제안

## 2단계 프로세스

### Phase 1 - 분석 및 문서화

1. 관련된 모든 파일을 읽고 현재 구조와 변경 영향을 확인합니다.
2. 발견한 내용을 마크다운 문서로 작성합니다.
3. 문서에는 파일 경로, 라인 번호, 문제 또는 개선 사항, 심각도, 제안 수정 방법을 포함합니다.
4. 분석 문서를 사용자에게 제시합니다.

### Phase 2 - 수정 적용

1. 승인되었거나 사용자가 명시적으로 즉시 수정을 요청한 항목만 수정합니다.
2. 한 번에 하나씩 수정합니다.
3. 각 수정 후 관련 테스트를 실행합니다.
4. 문제가 생기면 즉시 보고하고 원인을 확인합니다.

## 예외

아래 작업은 분석 문서 승인 없이 바로 진행할 수 있습니다.

- 사용자가 명시적으로 즉시 수정을 요청한 경우
- 단순한 버그 수정: 오타, 명확한 로직 오류 등
- 테스트 코드 작성

## 개발 명령어

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
