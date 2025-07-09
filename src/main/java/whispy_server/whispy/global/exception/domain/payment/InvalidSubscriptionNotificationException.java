package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidSubscriptionNotificationException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidSubscriptionNotificationException();

    public InvalidSubscriptionNotificationException() {
        super(ErrorCode.INVALID_SUBSCRIPTION_NOTIFICATION);
    }
}
