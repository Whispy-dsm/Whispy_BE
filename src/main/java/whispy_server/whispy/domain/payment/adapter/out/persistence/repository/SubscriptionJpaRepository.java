package whispy_server.whispy.domain.payment.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;

import java.util.Optional;

/**
 * 구독 JPA 리포지토리.
 */
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    /**
     * 구매 토큰으로 구독을 조회한다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 엔티티
     */
    Optional<SubscriptionJpaEntity> findByPurchaseToken(String purchaseToken);

    /**
     * 이메일로 최신 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return 구독 엔티티
     */
    Optional<SubscriptionJpaEntity> findByEmail(String email);

    /**
     * entitlement 판정에 사용할 가장 최근 만료 시각의 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return 현재 구독 엔티티
     */
    Optional<SubscriptionJpaEntity> findFirstByEmailOrderByExpiryTimeDesc(String email);
}
