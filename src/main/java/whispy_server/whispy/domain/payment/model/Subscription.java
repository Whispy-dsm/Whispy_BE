package whispy_server.whispy.domain.payment.model;

import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

/**
 * 구독 도메인 모델 (애그리게잇).
 *
 * Google Play 구독 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 구독 ID
 * @param email 사용자 이메일
 * @param purchaseToken Google Play 구매 토큰
 * @param productType 상품 타입
 * @param purchaseTime 구매 일시
 * @param subscriptionState 구독 상태
 * @param autoRenewing 자동 갱신 여부
 * @param expiryTime 만료 일시
 */
@Aggregate
public record Subscription(
        Long id,
        String email,
        String purchaseToken,
        ProductType productType,
        LocalDateTime purchaseTime,
        SubscriptionState subscriptionState,
        Boolean autoRenewing,
        LocalDateTime expiryTime
) {

    /**
     * 구독이 활성 상태인지 확인합니다.
     *
     * @return 구독이 활성 상태이면 true, 아니면 false
     */
    public boolean isActive() {
        return switch (subscriptionState) {
            case ACTIVE, GRACE_PERIOD -> LocalDateTime.now().isBefore(expiryTime);
            case CANCELED -> LocalDateTime.now().isBefore(expiryTime);
            default -> false;
        };
    }
}
