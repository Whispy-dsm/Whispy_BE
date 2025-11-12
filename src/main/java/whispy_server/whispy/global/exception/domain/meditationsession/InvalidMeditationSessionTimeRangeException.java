package whispy_server.whispy.global.exception.domain.meditationsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidMeditationSessionTimeRangeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidMeditationSessionTimeRangeException();

    public InvalidMeditationSessionTimeRangeException() {
        super(ErrorCode.INVALID_MEDITATION_SESSION_TIME_RANGE);
    }
}
