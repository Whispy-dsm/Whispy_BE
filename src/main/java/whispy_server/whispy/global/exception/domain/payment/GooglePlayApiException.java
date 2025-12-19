package whispy_server.whispy.global.exception.domain.payment;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * Google Play API 호출 실패 예외.
 *
 * Google Play Developer API 호출 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class GooglePlayApiException extends WhispyException {

    /**
     * 원인 예외와 함께 Google Play API 호출 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public GooglePlayApiException(Throwable cause) {
        super(ErrorCode.GOOGLE_PLAY_API_ERROR, cause);
    }
}
