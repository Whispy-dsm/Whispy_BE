package whispy_server.whispy.domain.payment.model;

public record GooglePlaySubscriptionInfo(
        String purchaseToken,
        String linkedPurchaseToken,
        Integer paymentState,
        Long expiryTimeMillis,
        Long startTimeMillis,
        String obfuscatedExternalAccountId
) {}
