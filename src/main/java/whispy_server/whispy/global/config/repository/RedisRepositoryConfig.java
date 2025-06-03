package whispy_server.whispy.global.config.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "whispy_server.whispy.domain.auth.adapter.out.persistence.repository")

public class RedisRepositoryConfig {
}