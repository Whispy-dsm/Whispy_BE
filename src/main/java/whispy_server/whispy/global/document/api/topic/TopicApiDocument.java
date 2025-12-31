package whispy_server.whispy.global.document.api.topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.util.List;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * FCM 토픽 구독/해제/조회 API 를 정의하는 Swagger 인터페이스이다.
 * 토픽 컨트롤러가 구현하여 공통 문서 스펙을 재사용한다.
 */
@Tag(name = "TOPIC API", description = "토픽 구독 관련 API")
public interface TopicApiDocument {

    @Operation(
            summary = "토픽 구독",
            description = "특정 토픽을 구독합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)

    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "디바이스 토큰을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 구독 중인 토픽입니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "FCM 서비스 연결 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void subscribeTopic(
            @RequestBody(description = "토픽 구독 요청", required = true,
                    content = @Content(schema = @Schema(implementation = TopicSubscriptionRequest.class)))
            TopicSubscriptionRequest request
    );

    @Operation(
            summary = "토픽 구독 해제",
            description = "특정 토픽의 구독을 해제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 해제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "구독 중이지 않은 토픽이거나 디바이스 토큰을 찾을 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "FCM 서비스 연결 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void unsubscribeTopic(
            @RequestBody(description = "토픽 구독 해제 요청", required = true,
                    content = @Content(schema = @Schema(implementation = TopicSubscriptionRequest.class)))
            TopicSubscriptionRequest request
    );

    @Operation(
            summary = "내 토픽 구독 목록 조회",
            description = "현재 사용자가 구독 중인 토픽 목록을 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TopicSubscriptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<TopicSubscriptionResponse> getMySubscriptions();
}
