package whispy_server.whispy.global.exception.domain.auth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class EmailAlreadySentException extends WhispyException {
    public static final WhispyException EXCEPTION = new EmailAlreadySentException();

    private EmailAlreadySentException() {
        super(ErrorCode.EMAIL_ALREADY_SENT);
    }
}
