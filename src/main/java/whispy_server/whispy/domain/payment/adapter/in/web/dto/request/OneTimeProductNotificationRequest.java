package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 일회성 상품 알림 요청 DTO.
 *
 * Google Play에서 전송하는 일회성 상품 구매 알림입니다.
 *
 * @param version API 버전
 * @param notificationType 알림 타입
 * @param purchaseToken 구매 토큰
 * @param sku SKU
 */
@Schema(description = "일회성 상품 알림 요청")
public record OneTimeProductNotificationRequest(
        @Schema(description = "API 버전", example = "1.0")
        String version,
        @Schema(description = "알림 타입", example = "1")
        Integer notificationType,
        @Schema(description = "구매 토큰", example = "abcdefghijklmnop")
        String purchaseToken,
        @Schema(description = "SKU", example = "premium_month")
        String sku
) {}
