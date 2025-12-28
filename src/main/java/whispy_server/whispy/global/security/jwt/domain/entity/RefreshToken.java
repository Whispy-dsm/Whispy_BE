package whispy_server.whispy.global.security.jwt.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

/**
 * Redis 에 저장되는 리프레시 토큰 엔티티로, 토큰 값과 TTL 을 함께 관리한다.
 * 사용자 식별자(id)를 키로 사용해 다중 디바이스 제어와 무효화 처리에 활용한다.
 */
@RedisHash(value = "refreshToken")
@Getter
@AllArgsConstructor
public class RefreshToken {

    @Id
    Long id;

    @Indexed
    String token;

    @TimeToLive
    Long ttl;

    /**
     * 리프레시 토큰 문자열과 TTL 을 갱신한다.
     *
     * @param token 새로 발급된 리프레시 토큰
     * @param ttl   남은 수명(초)
     */
    public void update(String token, Long ttl){
        this.token = Objects.requireNonNull(token);
        this.ttl = ttl;
    }
}


