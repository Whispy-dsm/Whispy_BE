package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

/**
 * 구독 조회 아웃바운드 포트.
 */
public interface QuerySubscriptionPort {

    /**
     * 구매 토큰으로 구독을 조회한다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 정보
     */
    Optional<Subscription> findByPurchaseToken(String purchaseToken);

    /**
     * 이메일로 최신 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return 구독 정보
     */
    Optional<Subscription> findByEmail(String email);

    /**
     * entitlement 판정에 사용할 현재 구독을 조회한다.
     *
     * @param email 사용자 이메일
     * @return entitlement 판정용 현재 구독
     */
    Optional<Subscription> findCurrentSubscriptionByEmail(String email);
}
