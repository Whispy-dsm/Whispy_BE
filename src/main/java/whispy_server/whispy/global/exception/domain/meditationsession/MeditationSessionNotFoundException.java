package whispy_server.whispy.global.exception.domain.meditationsession;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.MEDITATION_SESSION_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class MeditationSessionNotFoundException extends WhispyException {
    public static final WhispyException EXCEPTION = new MeditationSessionNotFoundException();

    private MeditationSessionNotFoundException() {
        super(ErrorCode.MEDITATION_SESSION_NOT_FOUND);
    }
}
