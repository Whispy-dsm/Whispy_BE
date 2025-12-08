package whispy_server.whispy.domain.topic.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

/**
 * 토픽 구독 상태를 노출하는 응답 DTO.
 */
@Schema(description = "토픽 구독 응답")
public record TopicSubscriptionResponse(
        @Schema(description = "구독 ID", example = "1")
        Long id,
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "구독 여부", example = "true")
        boolean Subscribed
){

    public static TopicSubscriptionResponse from(TopicSubscription subscription) {
        return new TopicSubscriptionResponse(
                subscription.id(),
                subscription.email(),
                subscription.topic(),
                subscription.subscribed()
        );
    }
}
