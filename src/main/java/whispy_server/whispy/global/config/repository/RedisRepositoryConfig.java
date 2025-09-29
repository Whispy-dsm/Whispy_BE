package whispy_server.whispy.global.config.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "whispy_server.whispy.global.security.jwt.domain.repository")
public class RedisRepositoryConfig {
}