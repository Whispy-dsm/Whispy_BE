package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_PAYMENT_STATE 상황을 나타내는 도메인 예외.
 */
public class InvalidPaymentStateException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidPaymentStateException();

    public InvalidPaymentStateException() {
        super(ErrorCode.INVALID_PAYMENT_STATE);
    }
}
