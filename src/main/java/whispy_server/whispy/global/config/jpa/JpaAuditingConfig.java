package whispy_server.whispy.global.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 스프링 데이터 JPA의 Auditing 기능을 활성화하는 설정.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
