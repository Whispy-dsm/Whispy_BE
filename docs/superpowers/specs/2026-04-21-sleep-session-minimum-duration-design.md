# Sleep Session Minimum Duration Design

## Context

Sleep sessions currently reject records shorter than 60 seconds. The same shared minimum is used by sleep, focus, and meditation session validation.

The product should treat naps as valid sleep records. Because valid naps can be much shorter than one hour, raising the sleep minimum to 60 minutes would discard useful data.

## Decision

Set the minimum saveable sleep-session duration to 15 minutes.

Keep the existing 60-second shared minimum for focus and meditation. Sleep should use its own minimum duration constant so this policy change does not affect other session domains.

## Scope

In scope:

- Add a sleep-specific minimum duration constant of 900 seconds.
- Apply that constant to sleep-session request validation.
- Apply that constant to the sleep-session domain model validation.
- Update the sleep duration validation error message from 1 minute to 15 minutes.
- Update unit tests to cover the 15-minute boundary.

Out of scope:

- Changing existing persisted sleep-session data.
- Changing focus or meditation duration validation.
- Changing sleep statistics aggregation logic.
- Adding frontend copy or UI behavior.

## Expected Behavior

- A sleep session with `durationSeconds = 900` is accepted when the time range supports it.
- A sleep session with `durationSeconds = 899` is rejected.
- A 30-minute nap remains valid.
- An 8-hour overnight sleep remains valid.
- Focus and meditation sessions still allow records of 60 seconds or more.

## Implementation Shape

Use the existing validation locations:

- `SaveSleepSessionRequest` should use the sleep-specific minimum in its `@Min` validation.
- `SleepSession` should use the sleep-specific minimum in its constructor guard.
- `ErrorCode.INVALID_SLEEP_SESSION_DURATION` should describe the 15-minute sleep minimum.

Do not update `SessionValidationConstants.MIN_SESSION_DURATION_SECONDS` from 60 to 900, because that would also change focus and meditation behavior.

## Testing Plan

Update the sleep-session service unit tests:

- Replace the current "less than 1 minute fails" case with "less than 15 minutes fails."
- Add or preserve a passing boundary case for exactly 15 minutes.
- Keep the existing 30-minute and long-session success cases.

Run the focused sleep-session tests first. If these touch shared validation constants, also run focus and meditation save-session tests to prove their 60-second behavior did not change.

## Risks

- The frontend may still show old validation copy if it owns any local duration messages.
- API documentation examples should stay consistent with the new sleep-specific minimum.
- Analytics may include more nap records than before if users previously abandoned shorter-but-valid sleep attempts because the backend rejected them.
