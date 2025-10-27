package whispy_server.whispy.global.exception.domain.focussession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidFocusSessionDurationException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidFocusSessionDurationException();

    public InvalidFocusSessionDurationException() {
        super(ErrorCode.INVALID_FOCUS_SESSION_DURATION);
    }
}
