package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

public record PubSubMessageRequest(
        PubSubMessageDataRequest message,
        String subscription
) {}
