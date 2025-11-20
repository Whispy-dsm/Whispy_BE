package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "PubSub 메시지 요청")
public record PubSubMessageRequest(
        @Schema(description = "메시지 데이터")
        PubSubMessageDataRequest message,
        @Schema(description = "구독 이름", example = "projects/my-project/subscriptions/my-subscription")
        String subscription
) {}
