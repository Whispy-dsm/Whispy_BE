package whispy_server.whispy.global.exception.domain.oauth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * OAuth 일회용 코드가 없거나 만료된 경우 발생하는 예외.
 */
public class InvalidOrExpiredOauthCodeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidOrExpiredOauthCodeException();

    private InvalidOrExpiredOauthCodeException() {
        super(ErrorCode.INVALID_OR_EXPIRED_OAUTH_CODE);
    }
}
