package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

public record OneTimeProductNotificationRequest(
        String version,
        Integer notificationType,
        String purchaseToken,
        String sku
) {}
