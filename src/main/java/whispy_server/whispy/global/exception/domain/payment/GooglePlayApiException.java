package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.GOOGLE_PLAY_API_ERROR 상황을 나타내는 도메인 예외.
 */
public class GooglePlayApiException extends WhispyException {

    public static final WhispyException EXCEPTION = new GooglePlayApiException();

    public GooglePlayApiException() {
        super(ErrorCode.GOOGLE_PLAY_API_ERROR);
    }
}
