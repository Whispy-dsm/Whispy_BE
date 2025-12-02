package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PubSub 메시지 요청 DTO.
 *
 * Google Cloud Pub/Sub 웹훅 요청의 최상위 구조입니다.
 *
 * @param message 메시지 데이터
 * @param subscription 구독 이름
 */
@Schema(description = "PubSub 메시지 요청")
public record PubSubMessageRequest(
        @Schema(description = "메시지 데이터")
        PubSubMessageDataRequest message,
        @Schema(description = "구독 이름", example = "projects/my-project/subscriptions/my-subscription")
        String subscription
) {}
