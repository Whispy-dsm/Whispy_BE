package whispy_server.whispy.global.feign.google.dto.response;

/**
 * 구글 플레이 구독 상태 API 응답을 캡슐화한 DTO.
 */
public record GooglePlaySubscriptionInfoResponse(
        String purchaseToken,
        String linkedPurchaseToken,
        Integer paymentState,
        Long expiryTimeMillis,
        Long startTimeMillis,
        String obfuscatedExternalAccountId
) {
}
