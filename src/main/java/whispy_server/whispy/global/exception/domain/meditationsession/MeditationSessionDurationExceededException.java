package whispy_server.whispy.global.exception.domain.meditationsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class MeditationSessionDurationExceededException extends WhispyException {

    public static final WhispyException EXCEPTION = new MeditationSessionDurationExceededException();

    public MeditationSessionDurationExceededException() {
        super(ErrorCode.MEDITATION_SESSION_DURATION_EXCEEDED);
    }
}
