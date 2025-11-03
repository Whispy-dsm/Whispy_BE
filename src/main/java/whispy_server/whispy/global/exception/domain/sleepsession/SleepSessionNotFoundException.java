package whispy_server.whispy.global.exception.domain.sleepsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class SleepSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new SleepSessionNotFoundException();

    private SleepSessionNotFoundException() {
        super(ErrorCode.SLEEP_SESSION_NOT_FOUND);
    }
}
