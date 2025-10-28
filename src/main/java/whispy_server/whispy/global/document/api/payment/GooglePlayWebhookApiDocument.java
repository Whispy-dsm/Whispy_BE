package whispy_server.whispy.global.document.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.global.exception.error.ErrorResponse;

@Tag(name = "WEBHOOK API", description = "외부 서비스 웹훅 관련 API")
public interface GooglePlayWebhookApiDocument {

    @Operation(
            summary = "Google Play 웹훅 처리",
            description = "Google Play에서 전송되는 구매/구독 알림을 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "웹훅 처리 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 유효하지 않은 메시지 형식",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Google Play 인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "웹훅 처리 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 처리된 알림",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "메시지 디코딩 실패 또는 유효하지 않은 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Google Play API 연결 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "서비스 일시적 사용 불가",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    void handleGooglePlayNotification(
            @Parameter(description = "Google Play Pub/Sub 메시지", required = true)
            PubSubMessageRequest pubSubMessage
    );
}
