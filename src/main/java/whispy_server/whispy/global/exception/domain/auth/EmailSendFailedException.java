package whispy_server.whispy.global.exception.domain.auth;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 이메일 전송 실패 예외.
 *
 * SMTP 이메일 전송 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class EmailSendFailedException extends WhispyException {

    /**
     * 원인 예외와 함께 이메일 전송 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public EmailSendFailedException(Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, cause);
    }
}
