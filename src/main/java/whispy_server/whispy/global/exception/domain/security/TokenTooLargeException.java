package whispy_server.whispy.global.exception.domain.security;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.TOKEN_TOO_LARGE 상황을 나타내는 도메인 예외.
 */
public class TokenTooLargeException extends WhispyException {

    public static final WhispyException EXCEPTION = new TokenTooLargeException();

    public TokenTooLargeException(){
        super(ErrorCode.TOKEN_TOO_LARGE);
    }
}
