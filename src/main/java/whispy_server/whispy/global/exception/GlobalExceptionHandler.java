package whispy_server.whispy.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.exception.error.ErrorResponse;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

/**
 * Controller 단에서 발생한 예외를 ErrorResponse로 변환하는 글로벌 핸들러.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DiscordNotificationService discordNotificationService;
    private final ErrorNotificationHandler errorNotificationHandler;

    /**
     * 애플리케이션 정의 예외를 처리한다.
     */
    @ExceptionHandler(WhispyException.class)
    public ResponseEntity<ErrorResponse> handleWhispyException(WhispyException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode);

        errorNotificationHandler.handleWhispyException(e);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    /**
     * Validation 에러를 처리한다.
     * 클라이언트 입력값 검증 실패 시 발생하며, Discord 알림을 보내지 않는다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.of(400, "입력값이 유효하지 않습니다");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 파라미터 타입 불일치 에러를 처리한다.
     * 클라이언트가 잘못된 타입의 값을 전달한 경우 발생한다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorResponse response = ErrorResponse.of(400, "요청 파라미터 타입이 올바르지 않습니다");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 존재하지 않는 리소스(경로) 요청을 처리한다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        ErrorResponse response = ErrorResponse.of(404, "요청하신 리소스를 찾을 수 없습니다");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 처리되지 않은 일반 예외를 서버 오류로 응답한다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){

        errorNotificationHandler.handleExceptionException(e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 클라이언트가 연결을 중단한 경우를 처리한다.
     * 음악/비디오 스트리밍 시 브라우저가 연결을 끊는 정상적인 동작이므로
     * 로그만 남기고 알림을 보내지 않는다.
     */
    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException e) {
        log.debug("Client aborted connection: {}", e.getMessage());
    }
}
