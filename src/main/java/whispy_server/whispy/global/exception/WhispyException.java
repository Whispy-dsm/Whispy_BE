package whispy_server.whispy.global.exception;

import lombok.Getter;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 서비스 공통 예외 베이스 클래스.
 *
 * {@link ErrorCode} 를 함께 보유해 핸들링 시 상태코드를 판단한다.
 */
@Getter
public class WhispyException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * ErrorCode만 받는 생성자.
     *
     * @param errorCode 오류 코드
     */
    public WhispyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 원인 예외를 받는 생성자.
     * 외부 시스템 연동 실패 등 원인 추적이 필요한 경우 사용합니다.
     *
     * @param errorCode 오류 코드
     * @param cause 원인 예외
     */
    public WhispyException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
