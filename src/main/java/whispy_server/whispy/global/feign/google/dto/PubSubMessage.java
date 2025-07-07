package whispy_server.whispy.global.feign.google.dto;

public record PubSubMessage(
        PubSubMessageData message,
        String subscription
) {}
