package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class UnknownProductIdException extends WhispyException {

    public static final WhispyException EXCEPTION = new UnknownProductIdException();

    public UnknownProductIdException() {
        super(ErrorCode.UNKNOWN_PRODUCT_ID);
    }
}
