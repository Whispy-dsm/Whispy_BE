package whispy_server.whispy.global.document.api.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.NotificationResponse;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.response.UnreadCountResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "NOTIFICATION API", description = "알림 관련 API")
public interface NotificationApiDocument {

    @Operation(summary = "디바이스 토큰으로 알림 전송", description = "특정 디바이스 토큰들에게 알림을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void sendNotification(NotificationSendRequest request);

    @Operation(summary = "토픽으로 알림 전송", description = "특정 토픽을 구독한 사용자들에게 알림을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토픽 알림 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void sendToTopic(NotificationTopicSendRequest request);

    @Operation(summary = "전체 사용자에게 브로드캐스트", description = "모든 사용자에게 알림을 브로드캐스트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브로드캐스트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "브로드캐스트 권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void broadcastToAllUsers(NotificationTopicSendRequest request);

    @Operation(summary = "내 알림 목록 조회", description = "현재 사용자의 알림 목록을 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    Page<NotificationResponse> getMyNotifications(Pageable pageable);

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "현재 사용자의 읽지 않은 알림 개수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽지 않은 알림 개수 조회 성공",
                    content = @Content(schema = @Schema(implementation = UnreadCountResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    UnreadCountResponse getUnreadCount();

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 읽음 처리 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void markAsRead(@Parameter(description = "알림 ID", required = true, in = ParameterIn.PATH) Long notificationId);

    @Operation(summary = "모든 알림 읽음 처리", description = "현재 사용자의 모든 알림을 읽음 상태로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 알림 읽음 처리 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void markAllAsRead();

    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void deleteNotification(@Parameter(description = "알림 ID", required = true, in = ParameterIn.PATH) Long notificationId);

    @Operation(summary = "모든 알림 삭제", description = "현재 사용자의 모든 알림을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 알림 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void deleteAllNotifications();
}
