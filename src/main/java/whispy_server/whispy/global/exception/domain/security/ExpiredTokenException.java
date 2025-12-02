package whispy_server.whispy.global.exception.domain.security;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.EXPIRED_TOKEN 상황을 나타내는 도메인 예외.
 */
public class ExpiredTokenException extends WhispyException {

    public static final WhispyException EXCEPTION = new ExpiredTokenException();

    public ExpiredTokenException(){
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
