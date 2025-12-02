package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.SLEEP_SESSION_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class SleepSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new SleepSessionNotFoundException();

    private SleepSessionNotFoundException() {
        super(ErrorCode.SLEEP_SESSION_NOT_FOUND);
    }
}
