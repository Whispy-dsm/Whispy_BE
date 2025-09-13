package whispy_server.whispy.domain.payment.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    Optional<SubscriptionJpaEntity> findByPurchaseToken(String purchaseToken);

    Optional<SubscriptionJpaEntity> findByEmail(String email);

    Optional<SubscriptionJpaEntity> findByEmailAndSubscriptionState(String email, SubscriptionState state);
}
