package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.PURCHASE_NOTIFICATION_PROCESSING_FAILED 상황을 나타내는 도메인 예외.
 */
public class PurchaseNotificationProcessingFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new PurchaseNotificationProcessingFailedException();

    public PurchaseNotificationProcessingFailedException() {
        super(ErrorCode.PURCHASE_NOTIFICATION_PROCESSING_FAILED);
    }
}
