package whispy_server.whispy.global.exception.domain.music;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class MusicNotFoundException extends WhispyException {

    public static final MusicNotFoundException EXCEPTION = new MusicNotFoundException();

    private MusicNotFoundException() {
        super(ErrorCode.MUSIC_NOT_FOUND);
    }
}
