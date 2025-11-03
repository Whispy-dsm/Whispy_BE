package whispy_server.whispy.domain.payment.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.adapter.out.mapper.SubscriptionEntityMapper;
import whispy_server.whispy.domain.payment.adapter.out.persistence.repository.SubscriptionJpaRepository;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionSavePort, QuerySubscriptionPort {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionEntityMapper subscriptionEntityMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(Subscription subscription) {
        subscriptionJpaRepository.save( subscriptionEntityMapper.toEntity(subscription) );
    }

    @Override
    public Optional<Subscription> findByPurchaseToken(String purchaseToken) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByPurchaseToken(purchaseToken));
    }

    @Override
    public Optional<Subscription> findByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByEmail(email));
    }

    @Override
    public Optional<Subscription> findActiveSubscriptionByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(
                subscriptionJpaRepository.findByEmailAndSubscriptionState(email, SubscriptionState.ACTIVE)
        );
    }
}
