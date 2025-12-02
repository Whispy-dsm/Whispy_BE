package whispy_server.whispy.global.document.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

/**
 * 이메일 인증 코드 발송/검증/상태 조회를 문서화하는 Swagger 인터페이스.
 * 인증 컨트롤러가 구현하여 공통 응답 스펙을 제공한다.
 */
@Tag(name = "EMAIL VERIFICATION API", description = "이메일 인증 관련 API")
public interface EmailVerificationApiDocument {

    @Operation(summary = "이메일 인증 코드 발송", description = "지정된 이메일 주소로 6자리 인증 코드를 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패 (이메일 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 발송된 인증 코드가 있습니다. 발송된 인증 코드를 입력하거나 5분 후 다시 요청해주세요.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "이메일 발송 요청이 너무 빈번합니다. 1분 후 다시 시도해주세요.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "이메일 발송에 실패했습니다. 다시 시도해주세요.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void sendVerificationCode(SendEmailVerificationRequest request);

    @Operation(summary = "이메일 인증 코드 검증", description = "발송된 6자리 인증 코드를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 검증 성공/실패",
                    content = @Content(schema = @Schema(implementation = VerifyEmailCodeResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패 (이메일 형식 오류, 인증 코드 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    VerifyEmailCodeResponse verifyCode(VerifyEmailCodeRequest request);

    @Operation(summary = "이메일 인증 상태 확인", description = "해당 이메일의 인증 완료 여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 상태 조회 성공",
                    content = @Content(schema = @Schema(implementation = CheckEmailVerificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패 (이메일 형식 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CheckEmailVerificationResponse checkVerificationStatus(CheckEmailVerificationRequest request);
}
