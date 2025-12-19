package whispy_server.whispy.global.exception.domain.fcm;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * FCM 메시지 전송 실패 예외.
 *
 * Firebase Cloud Messaging 전송 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class FcmSendFailedException extends WhispyException {

    /**
     * 원인 예외와 함께 FCM 전송 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public FcmSendFailedException(Throwable cause) {
        super(ErrorCode.FCM_SEND_FAILED, cause);
    }
}
