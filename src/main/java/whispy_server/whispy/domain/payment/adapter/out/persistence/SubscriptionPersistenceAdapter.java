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

/**
 * 구독 영속성 어댑터.
 *
 * 구독 저장 및 조회 포트를 구현하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionSavePort, QuerySubscriptionPort {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionEntityMapper subscriptionEntityMapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 구독을 저장합니다.
     *
     * @param subscription 저장할 구독 정보
     */
    @Override
    public void save(Subscription subscription) {
        subscriptionJpaRepository.save( subscriptionEntityMapper.toEntity(subscription) );
    }

    /**
     * 구매 토큰으로 구독을 조회합니다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 정보 (존재하지 않으면 empty)
     */
    @Override
    public Optional<Subscription> findByPurchaseToken(String purchaseToken) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByPurchaseToken(purchaseToken));
    }

    /**
     * 이메일로 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 정보 (존재하지 않으면 empty)
     */
    @Override
    public Optional<Subscription> findByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(subscriptionJpaRepository.findByEmail(email));
    }

    /**
     * 이메일로 활성 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 활성 구독 정보 (존재하지 않으면 empty)
     */
    @Override
    public Optional<Subscription> findActiveSubscriptionByEmail(String email) {
        return subscriptionEntityMapper.toOptionalModel(
                subscriptionJpaRepository.findByEmailAndSubscriptionState(email, SubscriptionState.ACTIVE)
        );
    }
}
