package whispy_server.whispy.global.config.redis.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import whispy_server.whispy.domain.statistics.sleep.summary.adapter.in.web.dto.response.SleepStatisticsResponse;
import whispy_server.whispy.global.config.redis.RedisConfig;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RedisConfig 캐시 직렬화 테스트")
class RedisConfigCacheSerializationTest {

    @Test
    @DisplayName("cache ObjectMapper는 LocalTime 포함 DTO를 직렬화/역직렬화한다")
    void cacheObjectMapperSerializesAndDeserializesLocalTime() throws Exception {
        RedisConfig redisConfig = new RedisConfig();
        Method cacheObjectMapperMethod = RedisConfig.class.getDeclaredMethod("cacheObjectMapper");
        cacheObjectMapperMethod.setAccessible(true);

        ObjectMapper cacheObjectMapper = (ObjectMapper) cacheObjectMapperMethod.invoke(redisConfig);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(cacheObjectMapper);

        SleepStatisticsResponse response = new SleepStatisticsResponse(
                480,
                450,
                0.85,
                LocalTime.of(23, 0),
                LocalTime.of(7, 0),
                13500,
                30
        );

        byte[] serialized = serializer.serialize(response);
        assertThat(serialized).isNotNull();

        Object deserialized = serializer.deserialize(serialized);
        assertThat(deserialized).isInstanceOf(SleepStatisticsResponse.class);
        SleepStatisticsResponse deserializedResponse = (SleepStatisticsResponse) deserialized;
        assertThat(deserializedResponse.averageBedTime()).isEqualTo(LocalTime.of(23, 0));
        assertThat(deserializedResponse.averageWakeTime()).isEqualTo(LocalTime.of(7, 0));
    }
}
