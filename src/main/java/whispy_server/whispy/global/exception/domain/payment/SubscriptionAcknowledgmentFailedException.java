package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 구독 승인 처리 실패 예외.
 *
 * Google Play 구독 승인 처리 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class SubscriptionAcknowledgmentFailedException extends WhispyException {

    /**
     * 원인 예외와 함께 구독 승인 처리 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public SubscriptionAcknowledgmentFailedException(Throwable cause) {
        super(ErrorCode.SUBSCRIPTION_ACKNOWLEDGMENT_FAILED, cause);
    }
}
