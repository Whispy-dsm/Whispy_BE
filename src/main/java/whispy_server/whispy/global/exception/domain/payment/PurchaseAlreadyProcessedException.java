package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class PurchaseAlreadyProcessedException extends WhispyException {

    public static final WhispyException EXCEPTION = new PurchaseAlreadyProcessedException();

    public PurchaseAlreadyProcessedException() {
        super(ErrorCode.PURCHASE_ALREADY_PROCESSED);
    }
}
