package whispy_server.whispy.global.exception.domain.auth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class EmailSendFailedException extends WhispyException {
    public static final WhispyException EXCEPTION = new EmailSendFailedException();

    private EmailSendFailedException() {
        super(ErrorCode.EMAIL_SEND_FAILED);
    }
}
