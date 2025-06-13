package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidKakaoOauthResponseException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidKakaoOauthResponseException();

    public InvalidKakaoOauthResponseException() {
        super(ErrorCode.INVALID_KAKAO_OAUTH_RESPONSE);
    }
}
