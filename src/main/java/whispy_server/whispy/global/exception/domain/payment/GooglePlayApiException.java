package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class GooglePlayApiException extends WhispyException {

    public static final WhispyException EXCEPTION = new GooglePlayApiException();

    public GooglePlayApiException() {
        super(ErrorCode.GOOGLE_PLAY_API_ERROR);
    }
}
