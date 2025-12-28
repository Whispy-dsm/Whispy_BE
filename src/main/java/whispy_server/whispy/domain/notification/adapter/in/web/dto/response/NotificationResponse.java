package whispy_server.whispy.domain.notification.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 알림 응답 DTO.
 *
 * 알림 상세 정보를 클라이언트에 전달하기 위한 응답 데이터입니다.
 *
 * @param id 알림 ID
 * @param email 사용자 이메일
 * @param title 알림 제목
 * @param body 알림 내용
 * @param topic 알림 주제
 * @param data 추가 데이터
 * @param read 읽음 여부
 * @param createdAt 생성 일시
 */
@Schema(description = "알림 응답")
public record NotificationResponse(
        @Schema(description = "알림 ID", example = "1")
        Long id,
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,
        @Schema(description = "알림 제목", example = "새로운 알림")
        String title,
        @Schema(description = "알림 내용", example = "알림 메시지 내용입니다")
        String body,
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "추가 데이터")
        Map<String, String> data,
        @Schema(description = "읽음 여부", example = "false")
        boolean read,
        @Schema(description = "생성 일시", example = "2024-01-01T12:00:00")
        LocalDateTime createdAt
) {

    /**
     * Notification 도메인 모델을 NotificationResponse로 변환합니다.
     *
     * @param notification 알림 도메인 모델
     * @return 알림 응답 DTO
     */
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.id(),
                notification.email(),
                notification.title(),
                notification.body(),
                notification.topic(),
                notification.data(),
                notification.read(),
                notification.createdAt()
        );
    }

    @Override
    public String toString() {
        return "NotificationResponse[id=" + id + ", email=***, title=" + title +
               ", body=" + body + ", topic=" + topic + ", data=" + data +
               ", read=" + read + ", createdAt=" + createdAt + "]";
    }
}
