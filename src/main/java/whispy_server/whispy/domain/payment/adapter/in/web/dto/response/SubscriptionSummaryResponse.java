package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;

/**
 * 구매 토큰을 제외한 사용자 구독 요약 응답 DTO.
 *
 * @param email 사용자 이메일
 * @param productType 상품 유형
 * @param purchaseTime 구매 시각
 * @param subscriptionState 구독 상태
 * @param autoRenewing 자동 갱신 여부
 * @param expiryTime 만료 시각
 */
@Schema(description = "사용자 구독 요약 응답")
public record SubscriptionSummaryResponse(
        @Schema(description = "사용자 이메일")
        String email,
        @Schema(description = "상품 유형")
        ProductType productType,
        @Schema(description = "구매 시각")
        LocalDateTime purchaseTime,
        @Schema(description = "구독 상태")
        SubscriptionState subscriptionState,
        @Schema(description = "자동 갱신 여부")
        Boolean autoRenewing,
        @Schema(description = "만료 시각")
        LocalDateTime expiryTime
) {

    /**
     * 도메인 구독을 요약 응답으로 변환한다.
     *
     * @param subscription 도메인 구독
     * @return 사용자 노출용 구독 요약
     */
    public static SubscriptionSummaryResponse from(Subscription subscription) {
        return new SubscriptionSummaryResponse(
                subscription.email(),
                subscription.productType(),
                subscription.purchaseTime(),
                subscription.subscriptionState(),
                subscription.autoRenewing(),
                subscription.expiryTime()
        );
    }
}
