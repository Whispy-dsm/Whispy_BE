package whispy_server.whispy.global.exception.domain.user;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.USER_ALREADY_EXIST 상황을 나타내는 도메인 예외.
 */
public class UserAlreadyExistException extends WhispyException {

    public static final WhispyException EXCEPTION = new UserAlreadyExistException();

    public  UserAlreadyExistException() {
        super(ErrorCode.USER_ALREADY_EXIST);
    }
}
