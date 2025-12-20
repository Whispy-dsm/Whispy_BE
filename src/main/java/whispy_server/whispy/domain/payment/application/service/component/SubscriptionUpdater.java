package whispy_server.whispy.domain.payment.application.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.util.Optional;

/**
 * 구독 업데이터.
 *
 * 구독 상태를 업데이트하는 도메인 서비스입니다.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionUpdater {

    private final QuerySubscriptionPort querySubscriptionPort;
    private final SubscriptionSavePort subscriptionSavePort;
    private final SubscriptionFactory subscriptionFactory;

    /**
     * 구독 상태를 업데이트합니다.
     *
     * @param purchaseToken 구매 토큰
     * @param newState 새로운 상태
     */
    @Transactional
    public void updateState(String purchaseToken, SubscriptionState newState) {
        Optional<Subscription> subscriptionOpt = querySubscriptionPort.findByPurchaseToken(purchaseToken);
        subscriptionOpt.ifPresent(subscription -> {
            Subscription updated = subscriptionFactory.withState(subscription, newState);
            subscriptionSavePort.save(updated);
        });
    }
}
