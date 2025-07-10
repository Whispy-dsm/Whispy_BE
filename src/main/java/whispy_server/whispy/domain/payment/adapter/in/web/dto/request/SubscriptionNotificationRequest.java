package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

public record SubscriptionNotificationRequest(
        String version,
        Integer notificationType,
        String purchaseToken,
        String subscriptionId
) {}
