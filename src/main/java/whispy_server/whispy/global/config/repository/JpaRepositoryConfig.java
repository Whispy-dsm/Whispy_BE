package whispy_server.whispy.global.config.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 도메인 패키지의 JpaRepository 인터페이스를 스캔해 등록하는 설정.
 */
@Configuration
@EnableJpaRepositories(basePackages = "whispy_server.whispy.domain")
public class JpaRepositoryConfig {
}

