# Redis Cache Review

Date: 2026-04-13
Scope: Whispy backend Redis cache / Redis-backed state review

## Verdict

Redis는 **부분적으로는 잘 적용되어 있지만**, 현재 상태를 "운영에서 충분히 검증된 캐시"라고 보기는 어렵다.

좋은 점:
- Spring Cache가 실제 서비스 메서드에 연결되어 있다.
- 프로필, 음악 카테고리 검색, 집중/수면 요약 통계에 TTL이 잡혀 있다.
- 통계 캐시는 세션 저장/삭제 후 `afterCommit` 기반 버전 증가로 stale-data를 줄이려는 설계가 있다.
- 음악 카테고리 캐시는 생성/수정/삭제 시 전체 eviction이 연결되어 있다.

우려점:
- 캐시 annotation 동작 자체를 검증하는 통합 테스트가 없다.
- 통계 캐시 무효화 범위가 너무 넓다.
- Redis 캐시 직렬화 설정이 과하게 permissive 하다.

## What already exists

- Global Redis / CacheManager 설정: [RedisConfig.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/config/redis/RedisConfig.java):36
- 음악 카테고리 검색 캐시: [SearchMusicCategoryCacheService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/SearchMusicCategoryCacheService.java):21
- 내 프로필 캐시: [GetMyProfileService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/user/application/service/GetMyProfileService.java):30
- 프로필 변경 시 캐시 갱신: [ChangeProfileService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/user/application/service/ChangeProfileService.java):36
- 집중 통계 캐시: [GetFocusStatisticsService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/statistics/focus/summary/application/service/GetFocusStatisticsService.java):41
- 수면 통계 캐시: [GetSleepStatisticsService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/statistics/sleep/summary/application/service/GetSleepStatisticsService.java):47
- 통계 캐시 버전 키 생성: [StatisticsSummaryKeyGenerator.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java):17
- 통계 캐시 버전 증가: [StatisticsCacheVersionManager.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheVersionManager.java):19

Redis는 캐시 외에도 다음 용도로 사용된다.
- refresh token 저장소
- 이메일 인증 코드 / 인증 상태 저장
- OAuth one-time code 저장

## Step 0: Scope Challenge

1. Existing code already solving the problem
- Redis cache는 이미 핵심 일부에 들어가 있다. 새 캐시를 만드는 문제가 아니라 **현재 적용 범위와 무효화 품질을 검토**하는 문제다.

2. Minimum review scope
- Redis 설정
- `@Cacheable`, `@CachePut`, `@CacheEvict`
- 통계 버전 무효화
- 테스트 커버리지

3. Complexity check
- 리뷰 대상은 8개 이상 파일이지만, 구현 변경이 아니라 아키텍처 검토라 정상 범위다.

4. Search check
- Search unavailable — proceeding with in-distribution knowledge only.

5. Completeness check
- 이 리뷰는 "캐시가 있는가"가 아니라 "캐시가 정확히 붙고, stale-data 리스크가 통제되는가"까지 본다.

## Architecture Review

### 1. Statistics invalidation is correct in direction but too coarse
Severity: Medium

통계 요약 캐시는 사용자당 **단일 version key**를 공유한다. [StatisticsCacheVersionManager.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/cache/version/StatisticsCacheVersionManager.java):21 와 [StatisticsSummaryKeyGenerator.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java):31 를 보면 캐시 키가 `userId:period:date:v{version}` 구조인데, version 자체가 `stats:ver:user:{userId}` 하나뿐이다.

그래서 집중 세션 저장/삭제, 수면 세션 저장/삭제, 명상 세션 저장/삭제가 모두 같은 version을 올린다.
- [SaveFocusSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/focussession/application/service/SaveFocusSessionService.java):60
- [DeleteFocusSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/focussession/application/service/DeleteFocusSessionService.java):45
- [SaveSleepSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/sleepsession/application/service/SaveSleepSessionService.java):54
- [DeleteSleepSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/sleepsession/application/service/DeleteSleepSessionService.java):42
- [SaveMeditationSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/meditationsession/application/service/SaveMeditationSessionService.java):59
- [DeleteMeditationSessionService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/meditationsession/application/service/DeleteMeditationSessionService.java):43

결과:
- 집중 세션을 하나 저장하면 수면 요약 캐시도 같이 무효화된다.
- 명상 세션을 저장하면 집중/수면 요약 캐시도 같이 무효화된다.

정합성은 유지되지만, 히트율은 불필요하게 떨어진다.

Recommendation:
- 버전을 `focus`, `sleep`, `meditation` 별로 분리하는 것이 더 낫다.

### 2. Cache coverage is narrow but internally consistent
Severity: Low

Spring Cache가 붙은 메서드는 현재 4개뿐이다.
- [SearchMusicCategoryCacheService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/SearchMusicCategoryCacheService.java):21
- [GetMyProfileService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/user/application/service/GetMyProfileService.java):30
- [ChangeProfileService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/user/application/service/ChangeProfileService.java):36
- [GetFocusStatisticsService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/statistics/focus/summary/application/service/GetFocusStatisticsService.java):41
- [GetSleepStatisticsService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/statistics/sleep/summary/application/service/GetSleepStatisticsService.java):47

특히 음악 카테고리 검색은 첫 페이지(`pageNumber == 0`)만 캐시한다. [SearchMusicCategoryCacheService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/SearchMusicCategoryCacheService.java):24

이건 메모리 사용량을 통제하려는 선택으로 보이며, 잘못된 것은 아니다. 다만 "Redis 캐시가 전반적으로 잘 적용됐다"기보다 **핫패스 일부에만 제한적으로 적용됐다**고 보는 게 정확하다.

## Code Quality Review

### 3. Cache serializer is overly permissive
Severity: Medium

CacheManager는 `GenericJackson2JsonRedisSerializer`와 `DefaultTyping.EVERYTHING` 조합을 쓰고 있고, subtype validator는 `LaissezFaireSubTypeValidator`다. [RedisConfig.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/config/redis/RedisConfig.java):136, [RedisConfig.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/config/redis/RedisConfig.java):186, [RedisConfig.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/config/redis/RedisConfig.java):199

이 설정은 `LocalTime` 같은 타입 직렬화 문제를 피하는 데는 유용하지만:
- 클래스 구조 변경에 더 취약하고
- Redis 데이터가 오염되면 역직렬화 공격면이 넓어진다

현재 Redis를 완전 신뢰된 내부 저장소로 전제하면 당장 치명적이진 않지만, 장기적으로는 더 좁은 직렬화 전략이 낫다.

### 4. Music category cache implementation is careful
Severity: Positive

`Page` 전체를 직접 캐시하지 않고, `MusicCategorySearchCacheValue`로 content/totalElements만 저장한 뒤 복원한다. [MusicCategorySearchCacheValue.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/MusicCategorySearchCacheValue.java):8

이건 Spring Data `PageImpl` 직렬화 복잡도를 줄이는 합리적인 선택이다.

또한 음악 생성/수정/삭제 시 캐시 전체 eviction이 연결되어 있다.
- [CreateMusicService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/CreateMusicService.java):34
- [UpdateMusicService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/UpdateMusicService.java):38
- [DeleteMusicService.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/domain/music/application/service/DeleteMusicService.java):45

## Test Review

### Code Path Coverage

```
REDIS CACHE / STATE COVERAGE
============================
[+] RedisConfig
    ├── [★ TESTED]  LocalTime serialization only
    └── [GAP]       CacheManager TTL / transactionAware / cache names wiring

[+] musicCategorySearch cache
    ├── [GAP]       @Cacheable hit/miss
    ├── [GAP]       page 0 only condition
    └── [GAP]       create/update/delete eviction

[+] userMyProfile cache
    ├── [GAP]       @Cacheable hit/miss
    ├── [GAP]       @CachePut overwrite behavior
    └── [GAP]       stale value after profile change integration

[+] stats summary cache
    ├── [GAP]       focus cache hit/miss
    ├── [GAP]       sleep cache hit/miss
    ├── [GAP]       versioned key bump after commit
    └── [GAP]       unrelated stats invalidation scope
```

### 5. Tests cover business logic, not actual cache behavior
Severity: High

현재 확인된 테스트는 모두 Mockito 단위 테스트이거나 serializer 단위 테스트다.

- Serializer test only:
  - [RedisConfigCacheSerializationTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/global/config/redis/unit/RedisConfigCacheSerializationTest.java):15

- Music category tests mock the cache service itself:
  - [SearchMusicCategoryServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/music/application/service/unit/SearchMusicCategoryServiceTest.java):32
  - [SearchMusicCategoryServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/music/application/service/unit/SearchMusicCategoryServiceTest.java):39

- Profile tests are Mockito-only:
  - [GetMyProfileServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/user/application/service/unit/GetMyProfileServiceTest.java):28
  - [ChangeProfileServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/user/application/service/unit/ChangeProfileServiceTest.java):32

- Focus/Sleep summary tests are also Mockito-only:
  - [GetFocusStatisticsServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/statistics/focus/summary/application/service/unit/GetFocusStatisticsServiceTest.java):44

즉, 현재 테스트로는 아래를 보장하지 못한다.
- Redis에 실제로 캐시가 저장되는지
- 두 번째 호출이 DB/port를 우회하는지
- `@CacheEvict`가 실제로 비우는지
- `@CachePut`이 실제로 덮어쓰는지
- 통계 버전 증가가 summary cache key에 실제 반영되는지

이건 “캐시가 잘 적용됐다”고 말하기 어렵게 만드는 가장 큰 근거다.

### 6. Invalidation intent exists, but tests do not prove it
Severity: Medium

세션 저장/삭제 서비스는 `StatisticsCacheVersionManager`를 주입받고 있다.
- [SaveFocusSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/SaveFocusSessionServiceTest.java):52
- [DeleteFocusSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/focussession/application/service/unit/DeleteFocusSessionServiceTest.java):49
- [SaveSleepSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/SaveSleepSessionServiceTest.java):42
- [DeleteSleepSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/sleepsession/application/service/unit/DeleteSleepSessionServiceTest.java):49
- [SaveMeditationSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/SaveMeditationSessionServiceTest.java):52
- [DeleteMeditationSessionServiceTest.java](C:/Users/user/Desktop/Whispy_BE/src/test/java/whispy_server/whispy/domain/meditationsession/application/service/unit/DeleteMeditationSessionServiceTest.java):49

하지만 이 테스트들은 version bump 호출을 검증하지 않는다. 따라서 “무효화가 연결돼 있다”는 건 코드 읽기 기준 사실이지만, 테스트 기준 사실은 아니다.

## Performance Review

### 7. Statistics caches likely help, but broad version invalidation reduces hit ratio
Severity: Medium

집중/수면 summary는 읽기 비용이 꽤 있는 집계 쿼리라 캐시 자체는 맞는 방향이다. 다만 사용자당 단일 version key는 unrelated writes까지 전부 같은 캐시 무효화로 이어진다. [StatisticsSummaryKeyGenerator.java](C:/Users/user/Desktop/Whispy_BE/src/main/java/whispy_server/whispy/global/cache/key/StatisticsSummaryKeyGenerator.java):33

결과적으로:
- 집중 통계만 보는 사용자가 수면 세션 하나 저장해도 집중 통계 캐시를 다시 만든다.
- 명상 세션 저장이 focus/sleep summary cache hit ratio를 같이 깎는다.

## Failure Modes

1. Cache annotation silently not working
- Test coverage: 없음
- Error handling: 없음
- User impact: silent failure, DB만 계속 조회

2. `@CacheEvict` 미동작
- Test coverage: 없음
- Error handling: 없음
- User impact: stale category/profile/statistics 응답

3. Shared statistics version key causing excessive invalidation
- Test coverage: 없음
- Error handling: 필요 없음
- User impact: silent performance degradation

Critical gap:
- **Cache wiring/invalidation behavior is untested**, and failure 시 사용자는 기능 오류 대신 느린 응답 또는 stale-data를 보게 된다.

## NOT in scope

- Redis 인프라 운영 점검
  - 실제 Redis 서버 메모리/eviction policy/replica 상태는 코드 리뷰 범위 밖이다.
- Redis 보안 네트워크 구성 점검
  - VPC/방화벽/접근제어는 인프라 문서 없이는 판단 불가다.
- 캐시 확대 적용 제안
  - 현재 리뷰는 “잘 적용됐는지” 확인이지, 새로운 캐시 지점 설계가 아니다.

## Recommendations

1. Add one Spring integration test per cache family
- `musicCategorySearch`
- `userMyProfile`
- `statsFocusSummary`
- `statsSleepSummary`

2. Verify invalidation explicitly
- profile change 후 cache overwrite
- music create/update/delete 후 category cache miss
- focus/sleep/meditation save/delete 후 summary key version increment

3. Split statistics cache version by domain
- `stats:ver:user:{userId}:focus`
- `stats:ver:user:{userId}:sleep`
- `stats:ver:user:{userId}:meditation`

4. Revisit cache serializer policy
- 최소한 cache payload 타입을 더 좁게 가져갈 수 있는지 검토

## Completion Summary

- Step 0: Scope Challenge — scope accepted as-is
- Architecture Review: 2 issues found
- Code Quality Review: 2 issues found
- Test Review: diagram produced, 7 gaps identified
- Performance Review: 1 issue found
- NOT in scope: written
- What already exists: written
- TODOS.md updates: not proposed in this review
- Failure modes: 1 critical gap flagged
- Outside voice: skipped
- Lake Score: 1/1 recommendations chose complete option
