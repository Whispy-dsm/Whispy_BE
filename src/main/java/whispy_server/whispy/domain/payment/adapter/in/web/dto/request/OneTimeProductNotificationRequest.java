package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

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
