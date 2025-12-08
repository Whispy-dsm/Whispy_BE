package whispy_server.whispy.domain.payment.model;

/**
 * Google Play 구독 정보.
 *
 * Google Play Billing API로부터 받아온 구독 상세 정보를 담고 있는 도메인 모델입니다.
 *
 * @param purchaseToken 구매 토큰
 * @param linkedPurchaseToken 연결된 구매 토큰 (업그레이드/다운그레이드 시)
 * @param paymentState 결제 상태
 * @param expiryTimeMillis 만료 시간 (밀리초)
 * @param startTimeMillis 시작 시간 (밀리초)
 * @param obfuscatedExternalAccountId 난독화된 외부 계정 ID
 */
public record GooglePlaySubscriptionInfo(
        String purchaseToken,
        String linkedPurchaseToken,
        Integer paymentState,
        Long expiryTimeMillis,
        Long startTimeMillis,
        String obfuscatedExternalAccountId
) {}
