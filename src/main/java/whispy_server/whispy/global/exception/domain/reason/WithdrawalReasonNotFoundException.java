package whispy_server.whispy.global.exception.domain.reason;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class WithdrawalReasonNotFoundException extends WhispyException {

    public static final WithdrawalReasonNotFoundException EXCEPTION = new WithdrawalReasonNotFoundException();

    private WithdrawalReasonNotFoundException() {
        super(ErrorCode.WITHDRAWAL_REASON_NOT_FOUND);
    }
}
