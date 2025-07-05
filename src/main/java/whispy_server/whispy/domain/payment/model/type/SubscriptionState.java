package whispy_server.whispy.domain.payment.model.type;

public enum SubscriptionState {

    ACTIVE,           // 정상 구독중 - 서비스 이용 가능
    CANCELED,         // 취소됨 (만료일까지 이용 가능) - 조건부 서비스 이용 가능
    EXPIRED,          // 만료됨 - 서비스 이용 불가
    PENDING,          // 결제 대기중 - 서비스 이용 불가
    PAUSED,           // 일시정지 - 서비스 이용 불가
    ON_HOLD,          // 결제 실패로 보류 - 서비스 이용 불가
    GRACE_PERIOD,     // 유예 기간 - 서비스 이용 가능
    REVOKED,          // 강제 취소/환불 - 서비스 이용 불가
    UPGRADED          // 상위 상품으로 업그레이드됨 - 서비스 이용 불가
}
