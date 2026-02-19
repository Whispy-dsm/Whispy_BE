package whispy_server.whispy.global.utils.redis;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StatisticsCacheVersionManager {

    private static final String USER_VERSION_KEY_PREFIX = "stats:ver:user:";
    private static final long INITIAL_VERSION = 0L;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 사용자 통계 캐시 버전을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 버전 값 (없으면 0)
     */
    public long getUserVersion(Long userId) {
        String rawValue = stringRedisTemplate.opsForValue().get(buildUserVersionKey(userId));
        if (rawValue == null) {
            return INITIAL_VERSION;
        }

        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException ignored) {
            return INITIAL_VERSION;
        }
    }

    /**
     * 사용자 통계 캐시 버전을 즉시 증가시킨다.
     *
     * @param userId 사용자 ID
     */
    public void bumpUserVersion(Long userId) {
        stringRedisTemplate.opsForValue().increment(buildUserVersionKey(userId));
    }

    /**
     * 트랜잭션 커밋 이후 사용자 통계 캐시 버전을 증가시킨다.
     *
     * @param userId 사용자 ID
     */
    public void bumpUserVersionAfterCommit(Long userId) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    bumpUserVersion(userId);
                }
            });
            return;
        }

        bumpUserVersion(userId);
    }

    private String buildUserVersionKey(Long userId) {
        return USER_VERSION_KEY_PREFIX + userId;
    }
}
