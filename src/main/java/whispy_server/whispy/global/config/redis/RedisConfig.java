package whispy_server.whispy.global.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 연결, RedisTemplate, Spring Cache를 구성하는 설정 클래스.
 *
 * 현재 캐시 정책:
 * - 기본 캐시 TTL: 5분
 * - musicCategorySearch 캐시 TTL: 10분
 * - userMyProfile 캐시 TTL: 10분
 * - statsFocusSummary 캐시 TTL: 5분
 * - statsSleepSummary 캐시 TTL: 5분
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 음악 카테고리 검색 결과 캐시명.
     */
    public static final String MUSIC_CATEGORY_SEARCH_CACHE = "musicCategorySearch";

    /**
     * 내 프로필 조회 캐시명.
     */
    public static final String USER_MY_PROFILE_CACHE = "userMyProfile";

    /**
     * 집중 통계 요약 캐시명.
     */
    public static final String STATS_FOCUS_SUMMARY_CACHE = "statsFocusSummary";

    /**
     * 수면 통계 요약 캐시명.
     */
    public static final String STATS_SLEEP_SUMMARY_CACHE = "statsSleepSummary";

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;

    /**
     * 단일 노드 Redis 커넥션 팩토리를 생성한다.
     *
     * @return Redis 연결 팩토리
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        if (password != null && !password.trim().isEmpty()) {
            config.setPassword(password);
        }

        return new LettuceConnectionFactory(config);
    }

    /**
     * 범용 RedisTemplate을 구성한다.
     *
     * 직렬화 정책:
     * - Key/HashKey: StringRedisSerializer
     * - Value/HashValue: GenericJackson2JsonRedisSerializer
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return 범용 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper());
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 문자열 전용 RedisTemplate 빈을 등록한다.
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return 문자열 전용 RedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * Redis 기반 CacheManager를 구성한다.
     *
     * 설정:
     * - 기본 TTL: 5분
     * - musicCategorySearch TTL: 10분
     * - userMyProfile TTL: 10분
     * - statsFocusSummary TTL: 5분
     * - statsSleepSummary TTL: 5분
     * - null 값 캐싱 비활성화
     * - 트랜잭션 인식 모드 활성화
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return Redis 기반 CacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper());

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                )
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(5));

        RedisCacheConfiguration musicCategoryConfig = defaultConfig.entryTtl(Duration.ofMinutes(10));
        RedisCacheConfiguration myProfileConfig = defaultConfig.entryTtl(Duration.ofMinutes(10));
        RedisCacheConfiguration statsSummaryConfig = defaultConfig.entryTtl(Duration.ofMinutes(5));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(Map.of(
                        MUSIC_CATEGORY_SEARCH_CACHE, musicCategoryConfig,
                        USER_MY_PROFILE_CACHE, myProfileConfig,
                        STATS_FOCUS_SUMMARY_CACHE, statsSummaryConfig,
                        STATS_SLEEP_SUMMARY_CACHE, statsSummaryConfig
                ))
                .transactionAware()
                .build();
    }

    /**
     * Redis 직렬화에 사용할 ObjectMapper를 생성한다.
     *
     * 설정:
     * - activateDefaultTyping: 다형성 타입 정보 포함
     * - FAIL_ON_UNKNOWN_PROPERTIES: 알 수 없는 필드 무시
     *
     * @return 설정된 ObjectMapper
     */
    private ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
