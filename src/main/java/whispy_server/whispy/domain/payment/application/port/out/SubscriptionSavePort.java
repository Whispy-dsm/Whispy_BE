package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.Subscription;

public interface SubscriptionSavePort {
    void save(Subscription subscription);
}
