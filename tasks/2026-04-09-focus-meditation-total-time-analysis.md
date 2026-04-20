# 집중/호흡 종료 후 오늘 총 시간 표시 문제 분석

## 분석 범위
- 증상: 집중 타이머 또는 호흡 종료 후 요약 화면에서 "오늘 총 시간"이 떠야 하는데, 현재 완료한 세션 시간처럼 보인다.
- 목표: 백엔드가 잘못된 값을 내려주는지, 아니면 클라이언트가 다른 필드를 잘못 표시하는지 구분한다.

## 확인한 사실

### 1. 집중 저장 응답은 세션 시간과 오늘 총 시간을 분리해서 만든다
- 근거
  - `SaveFocusSessionService.execute()`는 세션 저장 후 `todayTotalMinutes`를 별도로 계산한 뒤 응답에 넣는다.
  - 파일: `src/main/java/whispy_server/whispy/domain/focussession/application/service/SaveFocusSessionService.java`
  - 핵심 라인: 57-62, 74-79
- 해석
  - 백엔드 내부에서 `durationSeconds`를 그대로 "오늘 총 시간" 자리에 복사하는 구조는 아니다.

### 2. 집중 응답 DTO에는 두 필드가 아예 따로 존재한다
- 근거
  - `FocusSessionResponse`는 `durationSeconds`와 `todayTotalMinutes`를 별도 필드로 가진다.
  - 파일: `src/main/java/whispy_server/whispy/domain/focussession/adapter/in/web/dto/response/FocusSessionResponse.java`
  - 핵심 라인: 41, 56, 65-75
- 해석
  - 클라이언트가 정상적으로 응답을 읽으면 세션 시간과 오늘 총 시간을 구분할 수 있다.

### 3. 호흡(명상) 저장 응답도 동일한 구조다
- 근거
  - `SaveMeditationSessionService.execute()`도 저장 후 `todayTotalMinutes`를 계산해 응답에 넣는다.
  - 파일: `src/main/java/whispy_server/whispy/domain/meditationsession/application/service/SaveMeditationSessionService.java`
  - 핵심 라인: 56-61, 73-77
  - `MeditationSessionResponse` 역시 `durationSeconds`와 `todayTotalMinutes`를 분리한다.
  - 파일: `src/main/java/whispy_server/whispy/domain/meditationsession/adapter/in/web/dto/response/MeditationSessionResponse.java`
  - 핵심 라인: 41, 56, 65-75
- 해석
  - 집중과 호흡 둘 다 같은 증상이라면, 공통 클라이언트 매핑 로직 문제일 가능성이 더 높다.

### 4. 기존 단위 테스트는 "총 시간 != 방금 세션 시간" 시나리오를 이미 검증한다
- 근거
  - 집중 테스트는 세션 60분, 오늘 총 180분 상황을 만들어 `todayTotalMinutes == 180`을 검증한다.
  - 파일: `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/SaveFocusSessionServiceTest.java`
  - 핵심 라인: 127-136
  - 호흡 테스트는 세션 15분, 오늘 총 45분 상황을 만들어 `todayTotalMinutes == 45`를 검증한다.
  - 파일: `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/SaveMeditationSessionServiceTest.java`
  - 핵심 라인: 127-136
  - 실행 검증:
    - `./gradlew.bat test --tests whispy_server.whispy.domain.focussession.application.service.unit.SaveFocusSessionServiceTest --tests whispy_server.whispy.domain.meditationsession.application.service.unit.SaveMeditationSessionServiceTest`
    - 결과: 성공
- 해석
  - 현재 백엔드 로직은 총 시간을 세션 시간과 다르게 내려줄 수 있고, 그 의도가 테스트로 고정돼 있다.

### 5. JSON 응답 키는 snake_case다
- 근거
  - 전역 Jackson 설정이 `SNAKE_CASE`다.
  - 파일: `src/main/resources/application.yml`
  - 핵심 라인: 50-52
- 해석
  - 클라이언트는 `duration_seconds`와 `today_total_minutes`를 읽어야 한다.
  - 클라이언트가 camelCase를 기대하거나, `today_total_minutes` 대신 `duration_seconds`를 두 번 쓰면 지금 증상이 그대로 나온다.

## 현재 판단

### 가능성 1. 첫 세션이라서 두 값이 같아 보인 경우
- 예를 들어 오늘 첫 3분 세션이면:
  - 세션 시간 = 3분
  - 오늘 총 시간 = 3분
- 이 경우 `3분 / 태그 / 3분`은 정상이다.

### 가능성 2. 클라이언트가 총 시간 필드 대신 세션 시간 필드를 재사용하는 경우
- 특히 집중/호흡 둘 다 같은 증상이면 이 가능성이 높다.
- 확인 포인트
  - 종료 후 사용하는 API 응답이 무엇인지
  - 그 응답에서 `today_total_minutes`를 실제로 읽는지
  - 화면 ViewModel 변환에서 `duration_seconds`를 총 시간 자리에 넣지 않았는지

### 가능성 3. 백엔드 집계 자체 문제
- 현재 코드와 테스트만 보면 우선순위가 낮다.
- 다만 실제 운영 데이터에서 두 번째, 세 번째 세션 이후에도 계속 동일값이 나온다면 그때는 저장 API 실응답 캡처가 필요하다.

## 결론
- 현재 저장 응답 계약만 보면 백엔드가 "오늘 총 시간" 대신 "방금 완료한 세션 시간"을 내려주는 구조는 아니다.
- 가장 유력한 원인은 다음 둘 중 하나다.
  1. 오늘 첫 세션이라 총 시간이 우연히 세션 시간과 같다.
  2. 클라이언트가 `today_total_minutes` 대신 `duration_seconds`를 표시하고 있다.

## 다음 확인 순서
1. 같은 날 동일 사용자로 두 번째 세션을 완료한다.
2. `POST /focus-sessions` 또는 `POST /meditation-sessions` 응답 본문을 확인한다.
3. `duration_seconds`와 `today_total_minutes`가 서로 다른지 본다.
4. 응답은 다른데 UI만 같으면 클라이언트 수정으로 넘어간다.

## 이번 작업 범위
- 프로덕션 코드 수정 없음
- 테스트 추가 없음
- 분석 문서와 작업 로그만 업데이트
