package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 구독 알림 요청 DTO.
 *
 * Google Play에서 전송하는 구독 상태 변경 알림입니다.
 *
 * @param version API 버전
 * @param notificationType 알림 타입
 * @param purchaseToken 구매 토큰
 * @param subscriptionId 구독 ID
 */
@Schema(description = "구독 알림 요청")
public record SubscriptionNotificationRequest(
        @Schema(description = "API 버전", example = "1.0")
        String version,
        @Schema(description = "알림 타입", example = "1")
        Integer notificationType,
        @Schema(description = "구매 토큰", example = "abcdefghijklmnop")
        String purchaseToken,
        @Schema(description = "구독 ID", example = "premium_monthly")
        String subscriptionId
) {
    @Override
    public String toString() {
        return "SubscriptionNotificationRequest[version=" + version + ", notificationType=" + notificationType +
               ", purchaseToken=***, subscriptionId=" + subscriptionId + "]";
    }
}
