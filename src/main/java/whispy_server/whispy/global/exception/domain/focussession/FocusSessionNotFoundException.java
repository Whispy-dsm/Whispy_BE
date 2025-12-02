package whispy_server.whispy.global.exception.domain.focussession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FOCUS_SESSION_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class FocusSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new FocusSessionNotFoundException();

    private FocusSessionNotFoundException() {
        super(ErrorCode.FOCUS_SESSION_NOT_FOUND);
    }
}
