package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidKakaoAccessTokenException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidKakaoAccessTokenException();

    public InvalidKakaoAccessTokenException() {
        super(ErrorCode.INVALID_KAKAO_ACCESS_TOKEN);
    }
}
