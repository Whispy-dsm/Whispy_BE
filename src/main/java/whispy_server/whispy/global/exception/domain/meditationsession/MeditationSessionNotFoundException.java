package whispy_server.whispy.global.exception.domain.meditationsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class MeditationSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new MeditationSessionNotFoundException();

    private MeditationSessionNotFoundException() {
        super(ErrorCode.MEDITATION_SESSION_NOT_FOUND);
    }
}
