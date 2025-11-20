package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

@Schema(description = "토픽 구독 요청")
public record TopicSubscriptionRequest(
        @Schema(description = "알림 주제")
        NotificationTopic topic
) {}
