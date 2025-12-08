package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_SLEEP_SESSION_TIME_RANGE 상황을 나타내는 도메인 예외.
 */
public class InvalidSleepSessionTimeRangeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidSleepSessionTimeRangeException();

    public InvalidSleepSessionTimeRangeException() {
        super(ErrorCode.INVALID_SLEEP_SESSION_TIME_RANGE);
    }
}
