package whispy_server.whispy.global.config.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Querydsl 사용을 위한 JPAQueryFactory 빈을 등록하는 설정.
 */
@Configuration
public class QuerydslConfig {

    /**
     * JPAQueryFactory를 주입 가능한 빈으로 노출한다.
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
