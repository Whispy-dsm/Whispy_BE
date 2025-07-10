package whispy_server.whispy.domain.payment.application.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscriptionUpdater {

    private final QuerySubscriptionPort querySubscriptionPort;
    private final SubscriptionSavePort subscriptionSavePort;
    private final SubscriptionFactory subscriptionFactory;

    public void updateState(String purchaseToken, SubscriptionState newState) {
        Optional<Subscription> subscriptionOpt = querySubscriptionPort.findByPurchaseToken(purchaseToken);
        subscriptionOpt.ifPresent(subscription -> {
            Subscription updated = subscriptionFactory.withState(subscription, newState);
            subscriptionSavePort.save(updated);
        });
    }
}
