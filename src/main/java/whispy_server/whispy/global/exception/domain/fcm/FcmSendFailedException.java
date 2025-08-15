package whispy_server.whispy.global.exception.domain.fcm;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FcmSendFailedException extends WhispyException {
    public static final WhispyException EXCEPTION = new FcmSendFailedException();

    private FcmSendFailedException() {
        super(ErrorCode.FCM_SEND_FAILED);
    }
}
