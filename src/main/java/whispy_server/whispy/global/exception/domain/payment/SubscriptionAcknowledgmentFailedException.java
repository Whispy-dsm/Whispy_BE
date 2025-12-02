package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.SUBSCRIPTION_ACKNOWLEDGMENT_FAILED 상황을 나타내는 도메인 예외.
 */
public class SubscriptionAcknowledgmentFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new SubscriptionAcknowledgmentFailedException();

    public SubscriptionAcknowledgmentFailedException() {
        super(ErrorCode.SUBSCRIPTION_ACKNOWLEDGMENT_FAILED);
    }
}
