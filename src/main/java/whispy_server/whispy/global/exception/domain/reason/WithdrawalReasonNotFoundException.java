package whispy_server.whispy.global.exception.domain.reason;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.WITHDRAWAL_REASON_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class WithdrawalReasonNotFoundException extends WhispyException {

    public static final WithdrawalReasonNotFoundException EXCEPTION = new WithdrawalReasonNotFoundException();

    private WithdrawalReasonNotFoundException() {
        super(ErrorCode.WITHDRAWAL_REASON_NOT_FOUND);
    }
}
