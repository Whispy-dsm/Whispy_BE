package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.Subscription;

import java.util.Optional;

/**
 * 구독 조회 아웃바운드 포트.
 *
 * 데이터베이스에서 구독 정보를 조회하는 인터페이스입니다.
 */
public interface QuerySubscriptionPort {
    /**
     * 구매 토큰으로 구독을 조회합니다.
     *
     * @param purchaseToken 구매 토큰
     * @return 구독 정보 (존재하지 않으면 empty)
     */
    Optional<Subscription> findByPurchaseToken(String purchaseToken);

    /**
     * 이메일로 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 구독 정보 (존재하지 않으면 empty)
     */
    Optional<Subscription> findByEmail(String email);

    /**
     * 이메일로 활성 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 활성 구독 정보 (존재하지 않으면 empty)
     */
    Optional<Subscription> findActiveSubscriptionByEmail(String email);

}
