package whispy_server.whispy.global.document.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;

@Tag(name = "PURCHASE API", description = "구매 검증 관련 API")
public interface PurchaseApiDocument {

    @Operation(summary = "구매 검증", description = "Google Play 구매를 검증하고 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 검증 성공",
                    content = @Content(schema = @Schema(implementation = ValidatePurchaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "402", description = "결제 필요"),
            @ApiResponse(responseCode = "409", description = "이미 처리된 구매 또는 중복 구매"),
            @ApiResponse(responseCode = "410", description = "만료된 구매 토큰"),
            @ApiResponse(responseCode = "422", description = "Google Play 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생"),
            @ApiResponse(responseCode = "502", description = "Google Play API 연결 오류"),
            @ApiResponse(responseCode = "503", description = "Google Play 서비스 일시적 오류")
    })
    ValidatePurchaseResponse validatePurchase(ValidatePurchaseRequest request);
}
