package whispy_server.whispy.global.exception.domain.auth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.EMAIL_RATE_LIMIT_EXCEEDED 상황을 나타내는 도메인 예외.
 */
public class EmailRateLimitExceededException extends WhispyException {
    public static final WhispyException EXCEPTION = new EmailRateLimitExceededException();

    private EmailRateLimitExceededException() {
        super(ErrorCode.EMAIL_RATE_LIMIT_EXCEEDED);
    }
}
