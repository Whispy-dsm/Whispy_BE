package whispy_server.whispy.domain.payment.application.port.out;

import whispy_server.whispy.domain.payment.model.Subscription;

/**
 * 구독 저장 아웃바운드 포트.
 *
 * 데이터베이스에 구독 정보를 저장하는 인터페이스입니다.
 */
public interface SubscriptionSavePort {
    /**
     * 구독을 저장합니다.
     *
     * @param subscription 저장할 구독 정보
     */
    void save(Subscription subscription);
}
