package whispy_server.whispy.domain.payment.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 구독 JPA 레포지토리.
 *
 * 구독 엔티티에 대한 데이터베이스 접근을 담당하는 Spring Data JPA 레포지토리입니다.
 */
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    /**
     * 구매 토큰으로 구독을 조회합니다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 엔티티 (존재하지 않으면 empty)
     */
    Optional<SubscriptionJpaEntity> findByPurchaseToken(String purchaseToken);

    /**
     * 이메일로 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 엔티티 (존재하지 않으면 empty)
     */
    Optional<SubscriptionJpaEntity> findByEmail(String email);

    /**
     * 이메일과 구독 상태로 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @param state 구독 상태
     * @return 구독 엔티티 (존재하지 않으면 empty)
     */
    Optional<SubscriptionJpaEntity> findByEmailAndSubscriptionState(String email, SubscriptionState state);
}
