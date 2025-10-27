package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidSleepSessionTimeRangeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidSleepSessionTimeRangeException();

    public InvalidSleepSessionTimeRangeException() {
        super(ErrorCode.INVALID_SLEEP_SESSION_TIME_RANGE);
    }
}
