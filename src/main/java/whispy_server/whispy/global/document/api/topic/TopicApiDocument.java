package whispy_server.whispy.global.document.api.topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.TopicSubscriptionRequest;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;

import java.util.List;

@Tag(name = "TOPIC API", description = "토픽 구독 관련 API")
public interface TopicApiDocument {

    @Operation(summary = "토픽 구독", description = "특정 토픽을 구독합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "디바이스 토큰을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "409", description = "이미 구독 중인 토픽입니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생"),
            @ApiResponse(responseCode = "502", description = "FCM 서비스 연결 오류")
    })
    void subscribeTopic(TopicSubscriptionRequest request);

    @Operation(summary = "토픽 구독 해제", description = "특정 토픽의 구독을 해제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 해제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "구독 중이지 않은 토픽이거나 디바이스 토큰을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생"),
            @ApiResponse(responseCode = "502", description = "FCM 서비스 연결 오류")
    })
    void unsubscribeTopic(TopicSubscriptionRequest request);

    @Operation(summary = "내 토픽 구독 목록 조회", description = "현재 사용자가 구독 중인 토픽 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 구독 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TopicSubscriptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    List<TopicSubscriptionResponse> getMySubscriptions();
}
