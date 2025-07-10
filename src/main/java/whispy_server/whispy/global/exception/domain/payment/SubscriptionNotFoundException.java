package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class SubscriptionNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new SubscriptionNotFoundException();

    public SubscriptionNotFoundException() {
        super(ErrorCode.SUBSCRIPTION_NOT_FOUND);
    }
}
