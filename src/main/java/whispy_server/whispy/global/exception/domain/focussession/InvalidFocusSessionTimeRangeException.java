package whispy_server.whispy.global.exception.domain.focussession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_FOCUS_SESSION_TIME_RANGE 상황을 나타내는 도메인 예외.
 */
public class InvalidFocusSessionTimeRangeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidFocusSessionTimeRangeException();

    public InvalidFocusSessionTimeRangeException() {
        super(ErrorCode.INVALID_FOCUS_SESSION_TIME_RANGE);
    }
}
