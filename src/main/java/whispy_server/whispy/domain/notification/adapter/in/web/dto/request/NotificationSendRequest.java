package whispy_server.whispy.domain.notification.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

@Schema(description = "알림 전송 요청")
public record NotificationSendRequest(
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,
        @Schema(description = "디바이스 토큰 목록")
        List<String> deviceTokens,
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "알림 제목", example = "새로운 알림")
        String title,
        @Schema(description = "알림 내용", example = "알림 메시지 내용입니다")
        String body,
        @Schema(description = "추가 데이터")
        Map<String, String> data
) {}
