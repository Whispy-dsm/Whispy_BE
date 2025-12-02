package whispy_server.whispy.global.exception.domain.music;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 음악을 찾을 수 없을 때 발생하는 예외.
 *
 * 요청한 ID에 해당하는 음악이 데이터베이스에 존재하지 않을 때 발생합니다.
 * 싱글톤 패턴으로 구현되어 있으며, EXCEPTION 상수를 통해 재사용됩니다.
 */
public class MusicNotFoundException extends WhispyException {

    public static final MusicNotFoundException EXCEPTION = new MusicNotFoundException();

    /**
     * 음악을 찾을 수 없음을 나타내는 예외를 생성합니다.
     */
    private MusicNotFoundException() {
        super(ErrorCode.MUSIC_NOT_FOUND);
    }
}
