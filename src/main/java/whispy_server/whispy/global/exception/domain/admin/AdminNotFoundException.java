package whispy_server.whispy.global.exception.domain.admin;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class AdminNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new AdminNotFoundException();

    public AdminNotFoundException(){
        super(ErrorCode.ADMIN_NOT_FOUND);
    }
}
