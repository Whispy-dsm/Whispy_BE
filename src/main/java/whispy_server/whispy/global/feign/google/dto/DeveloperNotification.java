package whispy_server.whispy.global.feign.google.dto;

public record DeveloperNotification(
        String version,
        String packageName,
        Long eventTimeMillis,
        SubscriptionNotification subscriptionNotification,
        OneTimeProductNotification oneTimeProductNotification
) {}
