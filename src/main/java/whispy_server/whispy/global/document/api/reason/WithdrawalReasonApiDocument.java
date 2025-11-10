package whispy_server.whispy.global.document.api.reason;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "WITHDRAWAL REASON API", description = "탈퇴 사유 API")
public interface WithdrawalReasonApiDocument {

    @Operation(
            summary = "탈퇴 사유 저장",
            description = "회원 탈퇴 전에 탈퇴 사유를 저장합니다. 기타 선택 시 상세 내용이 필수입니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "탈퇴 사유 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void saveWithdrawalReason(
            @RequestBody(description = "탈퇴 사유 저장 요청", required = true,
                    content = @Content(schema = @Schema(implementation = SaveWithdrawalReasonRequest.class)))
            SaveWithdrawalReasonRequest request
    );
}
