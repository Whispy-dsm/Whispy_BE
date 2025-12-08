package whispy_server.whispy.global.document.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 관리자용 구독 조회/상태 확인 API 를 문서화한 Swagger 인터페이스이다.
 */
@Tag(name = "SUBSCRIPTION API", description = "구독 관련 API")
public interface SubscriptionApiDocument {

    @Operation(
            summary = "사용자 구독 목록 조회",
            description = "특정 사용자의 모든 구독 정보를 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetUserSubscriptionsResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이메일 형식 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 사용자 정보 접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    GetUserSubscriptionsResponse getUserSubscriptions(
            @Parameter(description = "사용자 이메일", required = true, example = "user@example.com")
            String email
    );

    @Operation(
            summary = "사용자 구독 상태 확인",
            description = "특정 사용자의 현재 구독 상태를 확인합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 상태 확인 성공",
                    content = @Content(schema = @Schema(implementation = CheckUserSubscriptionStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이메일 형식 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 사용자 정보 접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CheckUserSubscriptionStatusResponse isUserSubscribed(
            @Parameter(description = "사용자 이메일", required = true, example = "user@example.com")
            String email
    );
}
