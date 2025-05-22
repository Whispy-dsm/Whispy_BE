package whispy_server.whispy.global.exception.domain.security;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class InvalidJwtException extends WhispyException {

    public final static WhispyException EXCEPTION = new InvalidJwtException();

    public InvalidJwtException(){
        super(ErrorCode.INVALID_TOKEN);
    }
}
