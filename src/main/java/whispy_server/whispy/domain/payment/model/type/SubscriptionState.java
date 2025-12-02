package whispy_server.whispy.domain.payment.model.type;

/**
 * 구독 상태.
 *
 * Google Play 구독의 현재 상태를 나타내는 열거형입니다.
 */
public enum SubscriptionState {

    /** 정상 구독중 - 서비스 이용 가능 */
    ACTIVE,
    /** 취소됨 (만료일까지 이용 가능) */
    CANCELED,
    /** 만료됨 - 서비스 이용 불가 */
    EXPIRED,
    /** 결제 대기중 - 서비스 이용 불가 */
    PENDING,
    /** 일시정지 - 서비스 이용 불가 */
    PAUSED,
    /** 결제 실패로 보류 - 서비스 이용 불가 */
    ON_HOLD,
    /** 유예 기간 - 서비스 이용 가능 */
    GRACE_PERIOD,
    /** 강제 취소/환불 - 서비스 이용 불가 */
    REVOKED,
    /** 상위 상품으로 업그레이드됨 - 서비스 이용 불가 */
    UPGRADED,
    /** 하위 상품으로 다운그레이드됨 - 서비스 이용 불가 */
    DOWNGRADED
}
