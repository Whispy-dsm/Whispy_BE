package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidSleepSessionDurationException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidSleepSessionDurationException();

    public InvalidSleepSessionDurationException() {
        super(ErrorCode.INVALID_SLEEP_SESSION_DURATION);
    }
}
