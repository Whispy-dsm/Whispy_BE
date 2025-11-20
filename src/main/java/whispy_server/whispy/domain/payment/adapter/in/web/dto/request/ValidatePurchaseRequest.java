package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "구매 검증 요청")
public record ValidatePurchaseRequest(
        @Schema(description = "구매 토큰", example = "abcdefghijklmnop", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String purchaseToken,
        @Schema(description = "구독 ID", example = "premium_monthly", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String subscriptionId
) {}
