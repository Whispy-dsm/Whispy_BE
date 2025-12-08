package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

/**
 * 새로운 알림 토픽을 관리자 측에서 추가할 때 사용하는 요청 DTO.
 */
@Schema(description = "새 토픽 추가 요청")
public record AddNewTopicRequest(
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "기본 구독 여부", example = "true")
        boolean defaultSubscribed
) {}
