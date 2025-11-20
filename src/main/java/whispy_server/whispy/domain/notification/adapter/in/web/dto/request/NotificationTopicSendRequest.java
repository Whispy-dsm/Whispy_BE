package whispy_server.whispy.domain.notification.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Map;

@Schema(description = "토픽별 알림 전송 요청")
public record NotificationTopicSendRequest(
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "알림 제목", example = "새로운 공지")
        String title,
        @Schema(description = "알림 내용", example = "공지사항 내용입니다")
        String body,
        @Schema(description = "추가 데이터")
        Map<String, String> data
) {
}
