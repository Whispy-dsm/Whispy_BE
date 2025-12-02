package whispy_server.whispy.global.exception.domain.reason;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_WITHDRAWAL_REASON_DETAIL 상황을 나타내는 도메인 예외.
 */
public class InvalidWithdrawalReasonDetailException extends WhispyException {

    public static final InvalidWithdrawalReasonDetailException EXCEPTION = new InvalidWithdrawalReasonDetailException();

    private InvalidWithdrawalReasonDetailException() {
        super(ErrorCode.INVALID_WITHDRAWAL_REASON_DETAIL);
    }
}
