package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class SubscriptionAcknowledgmentFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new SubscriptionAcknowledgmentFailedException();

    public SubscriptionAcknowledgmentFailedException() {
        super(ErrorCode.SUBSCRIPTION_ACKNOWLEDGMENT_FAILED);
    }
}
