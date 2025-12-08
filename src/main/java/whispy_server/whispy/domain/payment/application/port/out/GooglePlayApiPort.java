package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;

/**
 * Google Play API 아웃바운드 포트.
 *
 * Google Play Billing API와 통신하는 인터페이스입니다.
 */
public interface GooglePlayApiPort {

    /**
     * Google Play에서 구독 정보를 조회합니다.
     *
     * @param subscriptionId 구독 ID
     * @param purchaseToken 구매 토큰
     * @return Google Play 구독 정보
     */
    GooglePlaySubscriptionInfo getSubscriptionInfo(String subscriptionId, String purchaseToken);

    /**
     * Google Play 구독을 확인(acknowledge)합니다.
     *
     * @param subscriptionId 구독 ID
     * @param purchaseToken 구매 토큰
     */
    void acknowledgeSubscription(String subscriptionId, String purchaseToken);
}
