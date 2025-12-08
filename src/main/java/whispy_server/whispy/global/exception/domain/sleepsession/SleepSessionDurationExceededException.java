package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.SLEEP_SESSION_DURATION_EXCEEDED 상황을 나타내는 도메인 예외.
 */
public class SleepSessionDurationExceededException extends WhispyException {

    public static final WhispyException EXCEPTION = new SleepSessionDurationExceededException();

    public SleepSessionDurationExceededException() {
        super(ErrorCode.SLEEP_SESSION_DURATION_EXCEEDED);
    }
}
