package whispy_server.whispy.global.exception.domain.security;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_TOKEN 상황을 나타내는 도메인 예외.
 */
public class InvalidJwtException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidJwtException();

    public InvalidJwtException(){
        super(ErrorCode.INVALID_TOKEN);
    }
}
