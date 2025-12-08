package whispy_server.whispy.global.exception.domain.user;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.USER_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class UserNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new UserNotFoundException();

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
