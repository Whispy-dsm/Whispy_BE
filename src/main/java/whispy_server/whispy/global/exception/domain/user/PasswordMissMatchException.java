package whispy_server.whispy.global.exception.domain.user;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class PasswordMissMatchException extends WhispyException {

    public static final WhispyException EXCEPTION = new PasswordMissMatchException();

    public PasswordMissMatchException() {
        super(ErrorCode.PASSWORD_MISS_MATCH_EXCEPTION);
    }
}
