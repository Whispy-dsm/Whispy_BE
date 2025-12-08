package whispy_server.whispy.global.exception.domain.focussession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FOCUS_SESSION_DURATION_EXCEEDED 상황을 나타내는 도메인 예외.
 */
public class FocusSessionDurationExceededException extends WhispyException {

    public static final WhispyException EXCEPTION = new FocusSessionDurationExceededException();

    public FocusSessionDurationExceededException() {
        super(ErrorCode.FOCUS_SESSION_DURATION_EXCEEDED);
    }
}
