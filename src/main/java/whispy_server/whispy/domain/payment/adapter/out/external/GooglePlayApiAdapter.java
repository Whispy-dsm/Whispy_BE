package whispy_server.whispy.domain.payment.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.global.google.GooglePlayProperties;
import whispy_server.whispy.global.feign.google.client.GooglePlayFeignClient;
import whispy_server.whispy.global.feign.google.dto.response.GooglePlaySubscriptionInfoResponse;

/**
 * Google Play API 어댑터.
 *
 * Google Play Billing API와 통신하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class GooglePlayApiAdapter implements GooglePlayApiPort {

    private final GooglePlayFeignClient googlePlayFeignClient;
    private final GooglePlayProperties googlePlayProperties;

    /**
     * Google Play에서 구독 정보를 조회합니다.
     *
     * @param subscriptionId 구독 ID
     * @param purchaseToken 구매 토큰
     * @return Google Play 구독 정보
     */
    @Override
    public GooglePlaySubscriptionInfo getSubscriptionInfo(String subscriptionId, String purchaseToken) {
        GooglePlaySubscriptionInfoResponse response = googlePlayFeignClient.getSubscriptionInfo(
                googlePlayProperties.packageName(),
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

    /**
     * Google Play 구독을 확인(acknowledge)합니다.
     *
     * @param subscriptionId 구독 ID
     * @param purchaseToken 구매 토큰
     */
    @Override
    public void acknowledgeSubscription(String subscriptionId, String purchaseToken) {
        googlePlayFeignClient.acknowledgeSubscription(
                googlePlayProperties.packageName(),
                subscriptionId,
                purchaseToken
        );
    }
}
