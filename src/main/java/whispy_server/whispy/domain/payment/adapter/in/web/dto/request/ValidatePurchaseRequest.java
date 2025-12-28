package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 구매 검증 요청 DTO.
 *
 * 클라이언트가 구매를 검증하기 위해 전송하는 요청입니다.
 *
 * @param purchaseToken 구매 토큰
 * @param subscriptionId 구독 ID
 */
@Schema(description = "구매 검증 요청")
public record ValidatePurchaseRequest(
        @Schema(description = "구매 토큰", example = "abcdefghijklmnop", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String purchaseToken,
        @Schema(description = "구독 ID", example = "premium_monthly", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String subscriptionId
) {
    @Override
    public String toString() {
        return "ValidatePurchaseRequest[purchaseToken=***, subscriptionId=" + subscriptionId + "]";
    }
}
