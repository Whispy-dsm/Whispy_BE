package whispy_server.whispy.global.exception.domain.admin;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.ADMIN_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class AdminNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new AdminNotFoundException();

    public AdminNotFoundException(){
        super(ErrorCode.ADMIN_NOT_FOUND);
    }
}
