package whispy_server.whispy.global.feign.google.dto;

public record OneTimeProductNotification(
        String version,
        Integer notificationType,
        String purchaseToken,
        String sku
) {}
