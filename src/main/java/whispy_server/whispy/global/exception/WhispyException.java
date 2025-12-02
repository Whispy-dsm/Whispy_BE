package whispy_server.whispy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 서비스 공통 예외 베이스 클래스.
 *
 * {@link ErrorCode} 를 함께 보유해 핸들링 시 상태코드를 판단한다.
 */
@Getter
@RequiredArgsConstructor
public class WhispyException extends RuntimeException{

    private final ErrorCode errorCode;
}
