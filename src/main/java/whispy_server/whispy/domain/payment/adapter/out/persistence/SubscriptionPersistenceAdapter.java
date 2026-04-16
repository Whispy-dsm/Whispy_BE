package whispy_server.whispy.domain.payment.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.adapter.out.mapper.SubscriptionEntityMapper;
import whispy_server.whispy.domain.payment.adapter.out.persistence.repository.SubscriptionJpaRepository;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

/**
 * 구독 저장/조회 퍼시스턴스 어댑터.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionSavePort, QuerySubscriptionPort {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionEntityMapper subscriptionEntityMapper;

    /**
     * 구독을 저장한다.
     *
     * @param subscription 저장할 구독
     */
    @Override
    public void save(Subscription subscription) {
        subscriptionJpaRepository.save(subscriptionEntityMapper.toEntity(subscription));
    }

    /**
     * 구매 토큰으로 구독을 조회한다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 정보
     */
    @Override
    public Optional<Subscription> findByPurchaseToken(String purchaseToken) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByPurchaseToken(purchaseToken));
    }

    /**
     * 이메일로 최신 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return 구독 정보
     */
    @Override
    public Optional<Subscription> findByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByEmail(email));
    }

    /**
     * entitlement 판정에 사용할 현재 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return entitlement 판정용 구독 정보
     */
    @Override
    public Optional<Subscription> findCurrentSubscriptionByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(
                subscriptionJpaRepository.findFirstByEmailOrderByExpiryTimeDesc(email)
        );
    }
}
