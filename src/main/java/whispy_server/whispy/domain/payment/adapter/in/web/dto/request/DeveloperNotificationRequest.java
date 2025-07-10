package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

public record DeveloperNotificationRequest(
        String version,
        String packageName,
        Long eventTimeMillis,
        SubscriptionNotificationRequest subscriptionNotification,
        OneTimeProductNotificationRequest oneTimeProductNotification
) {}
