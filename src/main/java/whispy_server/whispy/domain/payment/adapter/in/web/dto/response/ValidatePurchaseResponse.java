package whispy_server.whispy.domain.payment.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 구매 검증 응답 DTO.
 *
 * 구매 검증 결과를 반환합니다.
 *
 * @param isValid 구매 유효 여부
 * @param message 메시지
 */
@Schema(description = "구매 검증 응답")
public record ValidatePurchaseResponse(
        @Schema(description = "구매 유효 여부", example = "true")
        boolean isValid,
        @Schema(description = "메시지", example = "구매가 유효합니다")
        String message
) {}
