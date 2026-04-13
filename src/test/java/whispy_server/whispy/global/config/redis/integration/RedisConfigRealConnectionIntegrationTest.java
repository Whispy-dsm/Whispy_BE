package whispy_server.whispy.global.config.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import whispy_server.whispy.global.config.redis.RedisConfig;
import whispy_server.whispy.global.utils.redis.RedisUtil;

@DisplayName("RedisConfig 실제 Redis 연결 통합 테스트")
class RedisConfigRealConnectionIntegrationTest {

    private static final String REDIS_HOST = envOrDefault("REDIS_HOST", "localhost");
    private static final int REDIS_PORT = Integer.parseInt(envOrDefault("REDIS_PORT", "6379"));
    private static final String REDIS_PASSWORD = System.getenv("REDIS_PASSWORD");

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate stringRedisTemplate;

    @AfterEach
    void tearDown() {
        if (connectionFactory != null) {
            connectionFactory.destroy();
        }
    }

    @Test
    @DisplayName("실제 Redis 연결에서 template, util, cache manager 경로가 동작한다")
    void realRedisConnectionSupportsTemplatesUtilAndCacheManager() {
        Assumptions.assumeTrue(
                isRedisReachable(REDIS_HOST, REDIS_PORT),
                () -> "Real Redis is not reachable at %s:%d".formatted(REDIS_HOST, REDIS_PORT)
        );

        RedisConfig redisConfig = new RedisConfig();
        ReflectionTestUtils.setField(redisConfig, "host", REDIS_HOST);
        ReflectionTestUtils.setField(redisConfig, "port", REDIS_PORT);
        ReflectionTestUtils.setField(redisConfig, "password", REDIS_PASSWORD);

        try {
            connectionFactory = (LettuceConnectionFactory) redisConfig.redisConnectionFactory();
            connectionFactory.afterPropertiesSet();
            connectionFactory.start();
            stringRedisTemplate = redisConfig.stringRedisTemplate(connectionFactory);
        } catch (Exception exception) {
            Assumptions.assumeTrue(
                    false,
                    () -> "Unable to initialize RedisConnectionFactory against %s:%d (%s)"
                            .formatted(REDIS_HOST, REDIS_PORT, exception.getMessage())
            );
        }

        try (RedisConnection connection = connectionFactory.getConnection()) {
            assertThat(connection.ping()).isEqualToIgnoringCase("PONG");
        }

        RedisUtil redisUtil = new RedisUtil(stringRedisTemplate);
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(connectionFactory);
        RedisCacheManager cacheManager = redisConfig.cacheManager(connectionFactory);

        String utilKey = "test:redis-util:" + UUID.randomUUID();
        String templateKey = "test:redis-template:" + UUID.randomUUID();
        String cacheKey = "test:redis-cache:" + UUID.randomUUID();
        String cacheRedisKey = RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE + "::" + cacheKey;
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("name", "payload");
        payload.put("count", 7);

        try {
            redisUtil.set(utilKey, "value", Duration.ofSeconds(30));
            assertThat(redisUtil.get(utilKey)).isEqualTo("value");
            assertThat(redisUtil.hasKey(utilKey)).isTrue();
            assertThat(redisUtil.getExpire(utilKey, TimeUnit.SECONDS)).isBetween(1L, 30L);
            assertThat(redisUtil.setIfAbsent(utilKey, "other-value", Duration.ofSeconds(30))).isFalse();

            try {
                assertThat(redisUtil.getAndDelete(utilKey)).isEqualTo("value");
                assertThat(redisUtil.hasKey(utilKey)).isFalse();
            } catch (Exception exception) {
                assertThat(exception).hasRootCauseMessage("ERR unknown command 'GETDEL'");
                redisUtil.delete(utilKey);
                assertThat(redisUtil.hasKey(utilKey)).isFalse();
            }

            assertThat(redisUtil.setIfAbsent(utilKey, "recreated", Duration.ofSeconds(30))).isTrue();

            redisTemplate.opsForValue().set(templateKey, payload, Duration.ofSeconds(30));
            Object storedPayload = redisTemplate.opsForValue().get(templateKey);
            assertThat(storedPayload).isEqualTo(payload);

            Cache cache = cacheManager.getCache(RedisConfig.MUSIC_CATEGORY_SEARCH_CACHE);
            assertThat(cache).isNotNull();
            cache.put(cacheKey, payload);

            Cache.ValueWrapper cachedPayload = cache.get(cacheKey);
            assertThat(cachedPayload).isNotNull();
            assertThat(cachedPayload.get()).isEqualTo(payload);
            assertThat(stringRedisTemplate.getExpire(cacheRedisKey, TimeUnit.SECONDS))
                    .isBetween(1L, Duration.ofMinutes(10).toSeconds());
        } finally {
            deleteQuietly(utilKey, templateKey, cacheRedisKey);
        }
    }

    private void deleteQuietly(String... keys) {
        if (stringRedisTemplate == null) {
            return;
        }

        for (String key : keys) {
            if (key != null && !key.isBlank()) {
                stringRedisTemplate.delete(key);
            }
        }
    }

    private static boolean isRedisReachable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
