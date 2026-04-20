# Redis Cache Verification Report

## Scope
- Verify domain-split statistics cache versioning in `StatisticsCacheDomain`, `StatisticsCacheVersionManager`, and `StatisticsSummaryKeyGenerator`.
- Verify the focus, sleep, and meditation session save/delete services bump the correct statistics cache domain after commit.
- Verify the unit tests were updated to assert the correct domain-specific cache bump behavior.
- Verify `RedisCacheBehaviorIntegrationTest` exists and covers the intended Spring cache wiring.

## Changed files
- `src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheDomain.java` - new domain enum for statistics cache versioning.
- `src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheVersionManager.java` - per-domain user version keys and bump APIs.
- `src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java` - resolves the cache domain and reads the domain-specific version.
- `src/main/java/whispy_server/whispy/domain/focussession/application/service/SaveFocusSessionService.java` - bumps the focus cache domain after commit.
- `src/main/java/whispy_server/whispy/domain/focussession/application/service/DeleteFocusSessionService.java` - bumps the focus cache domain after commit.
- `src/main/java/whispy_server/whispy/domain/sleepsession/application/service/SaveSleepSessionService.java` - bumps the sleep cache domain after commit.
- `src/main/java/whispy_server/whispy/domain/sleepsession/application/service/DeleteSleepSessionService.java` - bumps the sleep cache domain after commit.
- `src/main/java/whispy_server/whispy/domain/meditationsession/application/service/SaveMeditationSessionService.java` - bumps the meditation cache domain after commit.
- `src/main/java/whispy_server/whispy/domain/meditationsession/application/service/DeleteMeditationSessionService.java` - bumps the meditation cache domain after commit.
- `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/SaveFocusSessionServiceTest.java` - unit coverage for focus cache bump wiring.
- `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/DeleteFocusSessionServiceTest.java` - unit coverage for focus cache bump wiring.
- `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java` - unit coverage for sleep cache bump wiring.
- `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/DeleteSleepSessionServiceTest.java` - unit coverage for sleep cache bump wiring.
- `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/SaveMeditationSessionServiceTest.java` - unit coverage for meditation cache bump wiring.
- `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/DeleteMeditationSessionServiceTest.java` - unit coverage for meditation cache bump wiring.
- `src/test/java/whispy_server/whispy/global/cache/integration/RedisCacheBehaviorIntegrationTest.java` - integration test for cache behavior and statistics domain split.

## Verification items
- Confirm statistics cache keys are domain-scoped, not shared across focus, sleep, and meditation summaries.
- Confirm `StatisticsSummaryKeyGenerator` resolves `FOCUS`, `SLEEP`, and `MEDITATION` from the proxied target service type.
- Confirm `bumpUserVersionAfterCommit(userId, domain)` is called with the correct domain in each session save/delete service.
- Confirm the updated unit tests assert the exact `StatisticsCacheDomain` passed to the cache version manager.
- Confirm `RedisCacheBehaviorIntegrationTest` exists and validates:
  - music category cache hit and eviction behavior
  - profile cache read/update behavior
  - focus cache invalidation does not affect sleep cache entries
- Confirm no stale code still uses the old shared statistics cache version path.

## Commands run
- `git status --short` -> checked during report preparation
- `rg -n "StatisticsCacheDomain|bumpUserVersion|getUserVersion|stats:ver" src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheVersionManager.java src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheDomain.java` -> checked during report preparation
- `rg -n "bumpUserVersion|StatisticsCacheDomain|SaveFocusSessionService|DeleteFocusSessionService|SaveSleepSessionService|DeleteSleepSessionService|SaveMeditationSessionService|DeleteMeditationSessionService" src/main/java/whispy_server/whispy/domain/focussession/application/service/SaveFocusSessionService.java src/main/java/whispy_server/whispy/domain/focussession/application/service/DeleteFocusSessionService.java src/main/java/whispy_server/whispy/domain/sleepsession/application/service/SaveSleepSessionService.java src/main/java/whispy_server/whispy/domain/sleepsession/application/service/DeleteSleepSessionService.java src/main/java/whispy_server/whispy/domain/meditationsession/application/service/SaveMeditationSessionService.java src/main/java/whispy_server/whispy/domain/meditationsession/application/service/DeleteMeditationSessionService.java` -> checked during report preparation
- `rg -n "RedisCacheBehaviorIntegrationTest|ConcurrentMapCacheManager|ResourcelessTransactionManager|focus|sleep|profile|music" src/test/java/whispy_server/whispy/global/cache/integration/RedisCacheBehaviorIntegrationTest.java` -> checked during report preparation
- `./gradlew.bat test --tests "whispy_server.whispy.global.cache.integration.RedisCacheBehaviorIntegrationTest"` -> passed
- `./gradlew.bat test --tests "whispy_server.whispy.domain.focussession.application.service.unit.SaveFocusSessionServiceTest" --tests "whispy_server.whispy.domain.focussession.application.service.unit.DeleteFocusSessionServiceTest" --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.SaveSleepSessionServiceTest" --tests "whispy_server.whispy.domain.sleepsession.application.service.unit.DeleteSleepSessionServiceTest" --tests "whispy_server.whispy.domain.meditationsession.application.service.unit.SaveMeditationSessionServiceTest" --tests "whispy_server.whispy.domain.meditationsession.application.service.unit.DeleteMeditationSessionServiceTest"` -> passed
- `./gradlew.bat build` -> passed

## Residual risks
- The integration test uses `ConcurrentMapCacheManager`, so it verifies Spring cache wiring but not Redis network/runtime behavior.
- The `build` output still includes an existing Spring Batch shutdown warning from `userItemReader.close`; the build passed, but that warning remains outside this cache change.
- Serializer and cache configuration risks remain outside the statistics domain split change.
- If additional statistics entry points still use a shared version key, they need separate verification.
- Proxy-target detection in `StatisticsSummaryKeyGenerator` should be rechecked if service proxying or class hierarchy changes.
