package whispy_server.whispy.global.config.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Redis Repository를 스캔해 Bean으로 등록하는 설정.
 */
@Configuration
@EnableRedisRepositories(basePackages = "whispy_server.whispy.global.security.jwt.domain.repository")
public class RedisRepositoryConfig {
}
