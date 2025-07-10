package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import java.util.Map;

public record PubSubMessageDataRequest(
        Map<String, String> attributes,
        String data,
        String messageId
) {}
