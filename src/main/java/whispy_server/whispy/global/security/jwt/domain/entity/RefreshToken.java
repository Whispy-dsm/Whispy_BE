package whispy_server.whispy.global.security.jwt.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

@RedisHash(value = "refreshToken")
@Getter
@AllArgsConstructor
public class RefreshToken {

    @Id
    String id;

    @Indexed
    String token;

    @TimeToLive
    Long ttl;

    public void update(String token, Long ttl){
        this.token = Objects.requireNonNull(token);
        this.ttl = ttl;
    }
}


