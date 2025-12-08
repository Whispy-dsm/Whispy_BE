package whispy_server.whispy.global.exception.domain.user;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.PASSWORD_MISS_MATCH 상황을 나타내는 도메인 예외.
 */
public class PasswordMissMatchException extends WhispyException {

    public static final WhispyException EXCEPTION = new PasswordMissMatchException();

    public PasswordMissMatchException() {
        super(ErrorCode.PASSWORD_MISS_MATCH);
    }
}
