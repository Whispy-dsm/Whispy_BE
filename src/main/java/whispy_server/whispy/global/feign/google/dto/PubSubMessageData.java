package whispy_server.whispy.global.feign.google.dto;

import java.util.Map;

public record PubSubMessageData(
        Map<String, String> attributes,
        String data,
        String messageId
) {}
