package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;

public interface GooglePlayApiPort {

    GooglePlaySubscriptionInfo getSubscriptionInfo(String subscriptionId, String purchaseToken);
    void acknowledgeSubscription(String subscriptionId, String purchaseToken);
}
