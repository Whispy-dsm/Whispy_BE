package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_KAKAO_OAUTH_RESPONSE 상황을 나타내는 도메인 예외.
 */
public class InvalidKakaoOauthResponseException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidKakaoOauthResponseException();

    public InvalidKakaoOauthResponseException() {
        super(ErrorCode.INVALID_KAKAO_OAUTH_RESPONSE);
    }
}
