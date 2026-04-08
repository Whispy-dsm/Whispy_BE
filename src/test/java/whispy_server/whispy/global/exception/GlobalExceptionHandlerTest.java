package whispy_server.whispy.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import whispy_server.whispy.global.exception.error.ErrorResponse;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * GlobalExceptionHandler 단위 테스트.
 */
@DisplayName("GlobalExceptionHandler 테스트")
class GlobalExceptionHandlerTest {

    /**
     * JSON 역직렬화 실패는 400으로 처리해야 한다.
     */
    @Test
    @DisplayName("HttpMessageNotReadableException은 400 응답으로 변환한다")
    void handleHttpMessageNotReadableException_returnsBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler(
                mock(DiscordNotificationService.class),
                mock(ErrorNotificationHandler.class)
        );
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "JSON parse error",
                mock(HttpInputMessage.class)
        );

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadableException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().message()).isEqualTo("요청 본문 형식이 올바르지 않습니다");
    }
}
