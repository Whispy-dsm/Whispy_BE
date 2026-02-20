package whispy_server.whispy.global.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 문자열 연산을 단순화한 유틸리티 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 지정한 키에 값을 설정하고 TTL을 적용한다.
     */
    public void set(String key, String value, Duration duration) {
        stringRedisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 키의 값을 조회한다.
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 키의 값을 조회하고 즉시 삭제한다.
     */
    public String getAndDelete(String key) {
        return stringRedisTemplate.opsForValue().getAndDelete(key);
    }

    /**
     * 키를 삭제한다.
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 키 존재 여부를 반환한다.
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 키의 남은 TTL을 조회한다.
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 키가 존재하지 않을 때만 값을 저장한다.
     *
     * @return 저장 성공 시 true
     */
    public boolean setIfAbsent(String key, String value, Duration duration) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, value, duration);
        return Boolean.TRUE.equals(result);
    }
}
