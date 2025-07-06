package whispy_server.whispy.domain.payment.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.adapter.out.entity.QSubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.adapter.out.mapper.SubscriptionEntityMapper;
import whispy_server.whispy.domain.payment.adapter.out.persistence.repository.SubscriptionJpaRepository;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.domain.user.adapter.out.entity.QUserJpaEntity;

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
        return subscriptionJpaRepository.findByPurchaseToken(purchaseToken)
                .map(subscriptionEntityMapper::toModel);
    }

    @Override
    public Optional<Subscription> findByEmail(String email) {
        SubscriptionJpaEntity entity = jpaQueryFactory
                .selectFrom(QSubscriptionJpaEntity.subscriptionJpaEntity)
                .join(QUserJpaEntity.userJpaEntity)
                .on(QSubscriptionJpaEntity.subscriptionJpaEntity.email.eq(QUserJpaEntity.userJpaEntity.email))
                .where(QSubscriptionJpaEntity.subscriptionJpaEntity.email.eq(email))
                .fetchFirst();

        return Optional.ofNullable(entity)
                .map(subscriptionEntityMapper::toModel);
    }

    @Override
    public Optional<Subscription> findActiveSubscriptionByEmail(String email) {
        SubscriptionJpaEntity entity = jpaQueryFactory
                .selectFrom(QSubscriptionJpaEntity.subscriptionJpaEntity)
                .join(QUserJpaEntity.userJpaEntity)
                .on(QSubscriptionJpaEntity.subscriptionJpaEntity.email.eq(QUserJpaEntity.userJpaEntity.email))
                .where(
                        QSubscriptionJpaEntity.subscriptionJpaEntity.email.eq(email)
                                .and(QSubscriptionJpaEntity.subscriptionJpaEntity.subscriptionState.eq(SubscriptionState.ACTIVE))
                )
                .fetchFirst();

        return Optional.ofNullable(entity)
                .map(subscriptionEntityMapper::toModel);
    }
}
