# Redis Cache Change Rationale

## Problem

The Redis cache layer had two structural issues:

1. Statistics invalidation was too coarse. One version key covered focus, sleep, and meditation summaries, so any write in one domain invalidated cache entries for the others.
2. Cache behavior was not proven end-to-end. Existing unit tests verified service logic, but not Spring cache wiring, annotation behavior, or invalidation boundaries.

## Why this design

The fix keeps the existing cache pattern but narrows the invalidation scope.

- Statistics versioning is split by domain so each family of summaries invalidates independently.
- The version manager and key generator stay centralized, which avoids three separate cache implementations and keeps key construction consistent.
- Session write services bump only their own domain version after commit, so the cache reflects the actual mutation boundary.
- Unit tests were strengthened to verify the correct domain-specific bump calls.
- `RedisCacheBehaviorIntegrationTest` covers the Spring cache path so the annotations, proxies, and eviction behavior are exercised together.

## Alternatives rejected

- Keep a single statistics version key. Rejected because it preserves the invalidation blast radius problem and reduces cache hit rate.
- Create separate key generators and managers per domain. Rejected because it adds duplication without improving behavior.
- Rely on Mockito-only tests. Rejected because they do not prove the cache layer is wired correctly.
- Use only Redis container tests. Rejected for this pass because the main gap was cache behavior coverage, not Redis connectivity itself.

## Files touched

- `src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheDomain.java`
- `src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheVersionManager.java`
- `src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java`
- `src/main/java/whispy_server/whispy/domain/focussession/application/service/SaveFocusSessionService.java`
- `src/main/java/whispy_server/whispy/domain/focussession/application/service/DeleteFocusSessionService.java`
- `src/main/java/whispy_server/whispy/domain/sleepsession/application/service/SaveSleepSessionService.java`
- `src/main/java/whispy_server/whispy/domain/sleepsession/application/service/DeleteSleepSessionService.java`
- `src/main/java/whispy_server/whispy/domain/meditationsession/application/service/SaveMeditationSessionService.java`
- `src/main/java/whispy_server/whispy/domain/meditationsession/application/service/DeleteMeditationSessionService.java`
- `src/test/java/whispy_server/whispy/global/cache/integration/RedisCacheBehaviorIntegrationTest.java`
- `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/SaveFocusSessionServiceTest.java`
- `src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/DeleteFocusSessionServiceTest.java`
- `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java`
- `src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/DeleteSleepSessionServiceTest.java`
- `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/SaveMeditationSessionServiceTest.java`
- `src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/DeleteMeditationSessionServiceTest.java`

## Follow-up considerations

- The Redis serializer policy is still broad; it should be tightened separately if cache payload shape or trust boundaries become a concern.
- If cache hit rate becomes important enough, split statistics keys further only when there is evidence that domain-level invalidation is still too broad.
- If future cache behavior changes, keep at least one Spring-level integration test per cache family so annotation regressions are caught early.
