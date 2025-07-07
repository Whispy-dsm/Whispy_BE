package whispy_server.whispy.global.feign.google.dto;

public record SubscriptionNotification(
        String version,
        Integer notificationType,
        String purchaseToken,
        String subscriptionId
) {}
