package whispy_server.whispy.global.feign.google.dto.response;

public record GooglePlaySubscriptionInfoResponse(
        String purchaseToken,
        String linkedPurchaseToken,
        Integer paymentState,
        Long expiryTimeMillis,
        Long startTimeMillis,
        String obfuscatedExternalAccountId
) {
}
