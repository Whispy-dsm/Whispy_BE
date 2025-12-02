package whispy_server.whispy.global.exception.domain.auth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.EMAIL_NOT_VERIFIED 상황을 나타내는 도메인 예외.
 */
public class EmailNotVerifiedException extends WhispyException {

    public static final WhispyException EXCEPTION = new EmailNotVerifiedException();

    public EmailNotVerifiedException() {
        super(ErrorCode.EMAIL_NOT_VERIFIED);
    }
}
