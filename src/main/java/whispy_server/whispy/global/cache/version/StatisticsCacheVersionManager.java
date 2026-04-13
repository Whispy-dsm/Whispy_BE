package whispy_server.whispy.global.cache.version;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 통계 캐시 버전을 관리하는 컴포넌트.
 *
 * 사용자별 버전 키를 증가시켜 통계 캐시를 자연 무효화합니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticsCacheVersionManager {

    private static final String USER_VERSION_KEY_PREFIX = "stats:ver:user:";
    private static final long INITIAL_VERSION = 0L;
    private static final Duration USER_VERSION_KEY_TTL = Duration.ofDays(90);

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 사용자 통계 캐시 버전을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 버전 값 (없으면 0)
     */
    public long getUserVersion(Long userId, StatisticsCacheDomain domain) {
        String rawValue = stringRedisTemplate.opsForValue().get(buildUserVersionKey(userId, domain));
        if (rawValue == null) {
            return INITIAL_VERSION;
        }

        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException e) {
            log.warn("통계 캐시 버전 키 파싱 실패 - userId: {}, rawValue: {}", userId, rawValue, e);
            return INITIAL_VERSION;
        }
    }

    /**
     * 사용자 통계 캐시 버전을 즉시 증가시킨다.
     *
     * @param userId 사용자 ID
     */
    public void bumpUserVersion(Long userId, StatisticsCacheDomain domain) {
        String key = buildUserVersionKey(userId, domain);
        stringRedisTemplate.opsForValue().increment(key);
        stringRedisTemplate.expire(key, USER_VERSION_KEY_TTL);
    }

    /**
     * 트랜잭션 커밋 이후 사용자 통계 캐시 버전을 증가시킨다.
     *
     * @param userId 사용자 ID
     */
    public void bumpUserVersionAfterCommit(Long userId, StatisticsCacheDomain domain) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    bumpUserVersion(userId, domain);
                }
            });
            return;
        }

        bumpUserVersion(userId, domain);
    }

    private String buildUserVersionKey(Long userId, StatisticsCacheDomain domain) {
        return USER_VERSION_KEY_PREFIX + userId + ":" + domain.name().toLowerCase();
    }
}
