package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface QuerySubscriptionPort {
    Optional<Subscription> findByPurchaseToken(String purchaseToken);
    Optional<Subscription> findByEmail(String email);
    Optional<Subscription> findActiveSubscriptionByEmail(String email);

}
