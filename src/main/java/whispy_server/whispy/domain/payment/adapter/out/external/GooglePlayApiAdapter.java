package whispy_server.whispy.domain.payment.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.global.config.google.GooglePlayConfig;
import whispy_server.whispy.global.feign.google.client.GooglePlayFeignClient;
import whispy_server.whispy.global.feign.google.dto.response.GooglePlaySubscriptionInfoResponse;

@Component
@RequiredArgsConstructor
public class GooglePlayApiAdapter implements GooglePlayApiPort {

    private final GooglePlayFeignClient googlePlayFeignClient;
    private final GooglePlayConfig googlePlayConfig;

    @Override
    public GooglePlaySubscriptionInfo getSubscriptionInfo(String subscriptionId, String purchaseToken) {
        GooglePlaySubscriptionInfoResponse response = googlePlayFeignClient.getSubscriptionInfo(
                googlePlayConfig.getPackageName(),
                subscriptionId,
                purchaseToken
        );

        return new GooglePlaySubscriptionInfo(
                response.purchaseToken(),
                response.linkedPurchaseToken(),
                response.paymentState(),
                response.expiryTimeMillis(),
                response.startTimeMillis(),
                response.obfuscatedExternalAccountId()
        );
    }

    @Override
    public void acknowledgeSubscription(String subscriptionId, String purchaseToken) {
        googlePlayFeignClient.acknowledgeSubscription(
                googlePlayConfig.getPackageName(),
                subscriptionId,
                purchaseToken
        );
    }
}
