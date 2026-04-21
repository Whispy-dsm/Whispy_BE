# Sleep Session Minimum Duration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Change only sleep-session minimum duration from 60 seconds to 15 minutes while keeping focus and meditation at 60 seconds.

**Architecture:** Keep validation at the existing request DTO and domain model boundaries. Add a sleep-specific constant beside the existing shared session constant so the sleep policy changes without affecting other session domains.

**Tech Stack:** Java 21, Spring Boot 3.4.5, Bean Validation `@Min`, JUnit 5, Mockito, AssertJ, Gradle.

---

## File Structure

- Modify: `src/main/java/whispy_server/whispy/global/constants/SessionValidationConstants.java`
  - Responsibility: session duration validation constants.
  - Add `MIN_SLEEP_SESSION_DURATION_SECONDS = 15 * 60`.
  - Keep `MIN_SESSION_DURATION_SECONDS = 60` unchanged for focus and meditation.

- Modify: `src/main/java/whispy_server/whispy/domain/sleepsession/adapter/in/web/dto/request/SaveSleepSessionRequest.java`
  - Responsibility: HTTP request validation for sleep-session save requests.
  - Change only the sleep request `@Min` value to the sleep-specific constant.
  - Update request documentation from 1 minute to 15 minutes.

- Modify: `src/main/java/whispy_server/whispy/domain/sleepsession/model/SleepSession.java`
  - Responsibility: domain-level sleep-session invariants.
  - Change only the minimum-duration guard to the sleep-specific constant.
  - Keep time-range and duration-vs-range guards unchanged.

- Modify: `src/main/java/whispy_server/whispy/global/exception/error/ErrorCode.java`
  - Responsibility: API error code and message catalog.
  - Change only `INVALID_SLEEP_SESSION_DURATION` message from 1 minute to 15 minutes.
  - Keep focus and meditation duration messages unchanged.

- Modify: `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java`
  - Responsibility: service-level sleep-session save behavior.
  - Add a passing 15-minute boundary test.
  - Change the current 59-second failure test to a 14-minute-59-second failure test.
  - Preserve the existing 30-minute nap success test.

---

### Task 1: Lock Sleep Duration Boundary Tests

**Files:**
- Modify: `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java`

- [ ] **Step 1: Add imports if needed**

No new imports should be required. The test class already imports `LocalDateTime`, AssertJ, Mockito, `SaveSleepSessionRequest`, `SleepSession`, and `InvalidSleepSessionDurationException`.

- [ ] **Step 2: Add a failing success test for exactly 15 minutes**

Add this test after `whenShortSleepSession_thenSavesSuccessfully()`:

```java
@Test
@DisplayName("15분 수면 세션을 저장할 수 있다")
void whenSleepSessionIsExactlyFifteenMinutes_thenSavesSuccessfully() {
    // given
    User user = createUser();
    LocalDateTime startedAt = LocalDateTime.of(2024, 1, 15, 14, 0);
    LocalDateTime endedAt = LocalDateTime.of(2024, 1, 15, 14, 15);
    int durationSeconds = 15 * 60;

    SaveSleepSessionRequest request = new SaveSleepSessionRequest(
            startedAt,
            endedAt,
            durationSeconds
    );

    SleepSession savedSession = new SleepSession(
            1L,
            TEST_USER_ID,
            startedAt,
            endedAt,
            durationSeconds,
            LocalDateTime.now()
    );

    given(userFacadeUseCase.currentUser()).willReturn(user);
    given(sleepSessionSavePort.save(any(SleepSession.class))).willReturn(savedSession);

    // when
    SleepSessionResponse response = saveSleepSessionService.execute(request);

    // then
    assertThat(response.durationSeconds()).isEqualTo(15 * 60);
    verify(statisticsCacheVersionManager).bumpUserVersionAfterCommit(TEST_USER_ID, StatisticsCacheDomain.SLEEP);
}
```

- [ ] **Step 3: Replace the old 59-second failure test with a 14m59s failure test**

Replace this existing test:

```java
@Test
@DisplayName("1분 미만 수면 세션을 저장하면 오류가 발생한다")
void whenDurationIsLessThanOneMinute_thenThrowsException() {
    // given
    User user = createUser();
    SaveSleepSessionRequest request = new SaveSleepSessionRequest(
            LocalDateTime.of(2024, 1, 15, 14, 0),
            LocalDateTime.of(2024, 1, 15, 14, 0, 59),
            59
    );

    given(userFacadeUseCase.currentUser()).willReturn(user);

    // when & then
    assertThatThrownBy(() -> saveSleepSessionService.execute(request))
            .isSameAs(InvalidSleepSessionDurationException.EXCEPTION);
    verifyNoInteractions(sleepSessionSavePort, statisticsCacheVersionManager);
}
```

with this test:

```java
@Test
@DisplayName("15분 미만 수면 세션을 저장하면 오류가 발생한다")
void whenDurationIsLessThanFifteenMinutes_thenThrowsException() {
    // given
    User user = createUser();
    SaveSleepSessionRequest request = new SaveSleepSessionRequest(
            LocalDateTime.of(2024, 1, 15, 14, 0),
            LocalDateTime.of(2024, 1, 15, 14, 14, 59),
            (15 * 60) - 1
    );

    given(userFacadeUseCase.currentUser()).willReturn(user);

    // when & then
    assertThatThrownBy(() -> saveSleepSessionService.execute(request))
            .isSameAs(InvalidSleepSessionDurationException.EXCEPTION);
    verifyNoInteractions(sleepSessionSavePort, statisticsCacheVersionManager);
}
```

- [ ] **Step 4: Run the focused sleep test and verify RED**

Run:

```powershell
.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --no-daemon --console=plain
```

Expected: FAIL. The 14m59s test should fail because the current implementation still accepts durations greater than or equal to 60 seconds.

- [ ] **Step 5: Keep the RED test diff uncommitted**

Do not commit a failing state. Leave the test changes in the working tree and continue to Task 2.

```powershell
git status --short
```

Expected: `SaveSleepSessionServiceTest.java` is modified and unstaged or staged locally, but no commit has been created yet.

---

### Task 2: Implement Sleep-Specific 15-Minute Validation

**Files:**
- Modify: `src/main/java/whispy_server/whispy/global/constants/SessionValidationConstants.java`
- Modify: `src/main/java/whispy_server/whispy/domain/sleepsession/adapter/in/web/dto/request/SaveSleepSessionRequest.java`
- Modify: `src/main/java/whispy_server/whispy/domain/sleepsession/model/SleepSession.java`
- Modify: `src/main/java/whispy_server/whispy/global/exception/error/ErrorCode.java`
- Test: `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java`

- [ ] **Step 1: Add the sleep-specific constant**

In `SessionValidationConstants.java`, keep the existing shared constant and add this new constant below it:

```java
/**
 * 저장 가능한 최소 수면 세션 지속 시간(초).
 */
public static final int MIN_SLEEP_SESSION_DURATION_SECONDS = 15 * 60;
```

The class should contain both constants:

```java
public static final int MIN_SESSION_DURATION_SECONDS = 60;

/**
 * 저장 가능한 최소 수면 세션 지속 시간(초).
 */
public static final int MIN_SLEEP_SESSION_DURATION_SECONDS = 15 * 60;
```

- [ ] **Step 2: Update sleep request DTO validation**

In `SaveSleepSessionRequest.java`, change the duration field documentation and `@Min` annotation to:

```java
/**
 * 수면 지속 시간(초 단위).
 * 15분 이상이어야 합니다.
 * 예: 28800 (8시간)
 */
@Schema(description = "지속 시간(초)", example = "28800", requiredMode = Schema.RequiredMode.REQUIRED)
@Min(SessionValidationConstants.MIN_SLEEP_SESSION_DURATION_SECONDS) int durationSeconds
```

Do not change focus or meditation request DTOs.

- [ ] **Step 3: Update sleep domain model validation**

In `SleepSession.java`, change only the minimum-duration guard:

```java
if (durationSeconds < SessionValidationConstants.MIN_SLEEP_SESSION_DURATION_SECONDS) {
    throw InvalidSleepSessionDurationException.EXCEPTION;
}
```

Do not change:

```java
if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
    throw InvalidSleepSessionTimeRangeException.EXCEPTION;
}

long actualSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
if (durationSeconds > actualSeconds) {
    throw SleepSessionDurationExceededException.EXCEPTION;
}
```

- [ ] **Step 4: Update the sleep validation error message**

In `ErrorCode.java`, change only this enum entry:

```java
INVALID_SLEEP_SESSION_DURATION(400, "수면시간은 15분 이상이어야 합니다."),
```

Keep these entries at 1 minute:

```java
INVALID_FOCUS_SESSION_DURATION(400, "집중시간은 1분 이상이어야 합니다."),
INVALID_MEDITATION_SESSION_DURATION(400, "명상시간은 1분 이상이어야 합니다."),
```

- [ ] **Step 5: Run the focused sleep test and verify GREEN**

Run:

```powershell
.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --no-daemon --console=plain
```

Expected: PASS.

- [ ] **Step 6: Commit the green implementation and tests**

Commit production files and the now-green test file:

```powershell
git add src/main/java/whispy_server/whispy/global/constants/SessionValidationConstants.java `
  src/main/java/whispy_server/whispy/domain/sleepsession/adapter/in/web/dto/request/SaveSleepSessionRequest.java `
  src/main/java/whispy_server/whispy/domain/sleepsession/model/SleepSession.java `
  src/main/java/whispy_server/whispy/global/exception/error/ErrorCode.java `
  src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java
$message = @'
feat : 수면 세션 최소 저장 기준을 15분으로 분리

낮잠을 정상 수면 기록으로 인정하되 실수 기록은 줄이기 위해
수면 도메인만 15분 하한을 사용한다. 집중과 명상은 기존 60초
하한을 유지한다.

Constraint: 기존 공통 세션 최소값은 집중/명상/수면이 함께 참조한다
Rejected: 공통 최소값을 900초로 상향 | 집중/명상 저장 정책까지 바뀐다
Confidence: high
Scope-risk: narrow
Directive: 수면 최소 지속시간 정책은 `MIN_SLEEP_SESSION_DURATION_SECONDS`로만 조정한다
Tested: `.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --no-daemon --console=plain`
Not-tested: 실제 모바일 클라이언트 저장 플로우
'@
Set-Content -Path .omx/commit-message-sleep-minimum.txt -Value $message -Encoding utf8
git commit -F .omx/commit-message-sleep-minimum.txt
Remove-Item .omx/commit-message-sleep-minimum.txt
```

---

### Task 3: Regression Verification

**Files:**
- Verify: `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java`
- Verify: `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/SaveFocusSessionServiceTest.java`
- Verify: `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/SaveMeditationSessionServiceTest.java`

- [ ] **Step 1: Run sleep/focus/meditation save-session tests**

Run:

```powershell
.\gradlew.bat test `
  --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" `
  --tests "whispy_server.whispy.domain.focussession.application.service.unit.SaveFocusSessionServiceTest" `
  --tests "whispy_server.whispy.domain.meditationsession.application.service.unit.SaveMeditationSessionServiceTest" `
  --no-daemon --console=plain
```

Expected: PASS. This proves sleep uses 15 minutes while focus and meditation still use 60 seconds.

- [ ] **Step 2: Run diff whitespace validation**

Run:

```powershell
git diff --check
```

Expected: no output except possible Windows line-ending warnings.

- [ ] **Step 3: Review changed files**

Run:

```powershell
git diff --stat
git diff
```

Expected changed production files:

```text
src/main/java/whispy_server/whispy/global/constants/SessionValidationConstants.java
src/main/java/whispy_server/whispy/domain/sleepsession/adapter/in/web/dto/request/SaveSleepSessionRequest.java
src/main/java/whispy_server/whispy/domain/sleepsession/model/SleepSession.java
src/main/java/whispy_server/whispy/global/exception/error/ErrorCode.java
```

Expected changed test file:

```text
src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java
```

- [ ] **Step 4: Record verification in `tasks/todo.md`**

Append a short task log:

```markdown
## 2026-04-21 Sleep session minimum duration

- [x] Add 15-minute sleep boundary tests.
- [x] Add sleep-specific minimum duration constant.
- [x] Apply sleep-specific minimum to request and domain validation.
- [x] Preserve focus and meditation 60-second validation.
- [x] Run focused regression tests.

### Review
- Sleep sessions now require at least 15 minutes.
- Focus and meditation keep the existing 60-second minimum.
- Verification:
  - `.\gradlew.bat test --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --tests "whispy_server.whispy.domain.focussession.application.service.unit.SaveFocusSessionServiceTest" --tests "whispy_server.whispy.domain.meditationsession.application.service.unit.SaveMeditationSessionServiceTest" --no-daemon --console=plain` PASS
  - `git diff --check` PASS
- Remaining risk: frontend may still show old sleep validation copy if it keeps local messages.
```

- [ ] **Step 5: Commit the task log**

Commit only `tasks/todo.md` if it changed:

```powershell
git add tasks/todo.md
$message = @'
docs : 수면 세션 최소 기준 검증 기록

수면 세션 최소 저장 기준 변경의 검증 명령과 남은 프론트
카피 리스크를 작업 로그에 남긴다.

Confidence: high
Scope-risk: narrow
Tested: 작업 로그 diff 검토
Not-tested: 애플리케이션 코드 변경 없음
'@
Set-Content -Path .omx/commit-message-sleep-minimum-log.txt -Value $message -Encoding utf8
git commit -F .omx/commit-message-sleep-minimum-log.txt
Remove-Item .omx/commit-message-sleep-minimum-log.txt
```

---

## Self-Review

- Spec coverage: The plan covers the 900-second sleep-specific constant, request validation, domain validation, error message, boundary tests, and focus/meditation regression tests.
- Placeholder scan: No `TBD`, `TODO`, incomplete edge-case instructions, or undefined method names remain.
- Type consistency: All code snippets use existing Java types and paths from the current repository. The new constant name is consistently `MIN_SLEEP_SESSION_DURATION_SECONDS`.
