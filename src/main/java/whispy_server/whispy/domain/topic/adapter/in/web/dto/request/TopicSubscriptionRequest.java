package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

/**
 * 특정 토픽에 대해 구독/구독해제 요청 시 사용하는 DTO.
 */
@Schema(description = "토픽 구독 요청")
public record TopicSubscriptionRequest(
        @Schema(description = "알림 주제", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        NotificationTopic topic
) {}
