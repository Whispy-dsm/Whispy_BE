package whispy_server.whispy.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
        ErrorResponse response = ErrorResponse.of(errorCode, errorCode.getMessage());

        errorNotificationHandler.handleWhispyException(e);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatusCode()));
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

    /**
     * 처리되지 않은 일반 예외를 서버 오류로 응답한다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){

        errorNotificationHandler.handleExceptionException(e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.of(errorCode, "Internal Server Error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
