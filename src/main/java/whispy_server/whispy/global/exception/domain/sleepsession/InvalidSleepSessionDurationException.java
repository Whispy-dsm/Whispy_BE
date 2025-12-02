package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_SLEEP_SESSION_DURATION 상황을 나타내는 도메인 예외.
 */
public class InvalidSleepSessionDurationException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidSleepSessionDurationException();

    public InvalidSleepSessionDurationException() {
        super(ErrorCode.INVALID_SLEEP_SESSION_DURATION);
    }
}
