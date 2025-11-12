package whispy_server.whispy.global.exception.domain.meditationsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidMeditationSessionDurationException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidMeditationSessionDurationException();

    public InvalidMeditationSessionDurationException() {
        super(ErrorCode.INVALID_MEDITATION_SESSION_DURATION);
    }
}
