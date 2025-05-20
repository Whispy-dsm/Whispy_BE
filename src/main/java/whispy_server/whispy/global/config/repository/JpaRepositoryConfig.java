package whispy_server.whispy.global.config.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "whispy_server.whispy.domain")
public class JpaRepositoryConfig {
}

