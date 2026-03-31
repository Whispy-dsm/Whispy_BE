# Draft: Caching Strategy for Whispy_BE

## Requirements (confirmed)
- User request: "이제 내 프로젝트에서 캐싱 전략을 어디다가 사용해야 할지 추처해줘"
- User instruction: maximize search effort and be exhaustive.
- Perform broad parallel context gathering before final recommendation.

## Technical Decisions
- Use parallel explore/librarian/oracle agents to gather codebase and external best-practice evidence.
- Use direct repository searches (grep/AST/glob) to map cache candidates.
- Keep this phase read-only and recommendation-oriented.

## Research Findings
- Existing Redis infrastructure is present:
  - `global/config/redis/RedisConfig.java` (RedisConnectionFactory, RedisTemplate, StringRedisTemplate)
  - `global/utils/redis/RedisUtil.java` utility wrapper
  - `global/config/repository/RedisRepositoryConfig.java` + refresh-token Redis repository usage
- No app-level Spring Cache usage found yet (`@EnableCaching`, `@Cacheable`, `@CacheEvict` absent in main code scan).
- High-value cache candidates confirmed by service paths:
  - Music search/list/detail: `domain/music/application/service/*Search*`, `GetMusicDetailService`
  - Announcement list/detail: `domain/announcement/application/service/QueryAllAnnouncementService`
  - Statistics endpoints: focus/sleep/activity summary+comparison services
  - User-scoped read paths: notification unread count/list, likes list/check, topic subscriptions, listening history
- Write paths requiring eviction hooks identified:
  - music create/update/delete services
  - announcement create/update/delete services
  - focus/sleep/meditation session save/delete services
  - notification read/delete flows and like toggle flows
- Risk notes:
  - Avoid caching auth/security-critical token validation flows
  - Use short TTL + strict userId keys for user-scoped caches
  - Prefer versioned keys for search/list invalidation over broad wildcard deletes
- OSS production pattern findings consolidated:
  - Conditional cache resolver + runtime condition (`@Cacheable` + `condition`)
  - Multi-cache coordinated eviction with `@Caching`
  - Per-cache TTL via `RedisCacheManager` map
  - `disableCachingNullValues()` and explicit serializer policy
  - `@CachePut` for update write-through where suitable
- Important caveat from both repo scan and prior architecture analysis:
  - Current Redis serialization uses broad `activateDefaultTyping`; should be hardened before broad cache rollout.

## Open Questions
- Desired consistency level per endpoint (strong consistency vs eventual within short TTL)?
- Which environment should prioritize latency reduction first (prod only vs all)?
- For logout semantics, is immediate access-token invalidation required or is short access-token TTL acceptable?

## Scope Boundaries
- INCLUDE: cache candidate recommendation map, priority ranking, invalidation guidance.
- EXCLUDE: implementation/code changes in this step.

## Candidate Priority Snapshot
- P1: Music search/detail, Announcement list/detail
- P2: Focus/Sleep/Activity statistics summary+comparison
- P3: User-scoped short-TTL reads (notifications, likes, subscriptions, history)

## Invalidation Boundaries (Draft)
- Music writes -> evict music detail/search/category caches
- Announcement writes -> evict announcement list/detail caches
- Session writes (focus/sleep/meditation) -> evict related statistics caches
- Notification/like writes -> evict unread/list/liked/check caches
