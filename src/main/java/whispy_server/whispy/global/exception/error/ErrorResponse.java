package whispy_server.whispy.global.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * API 오류 응답 본문을 표현하는 레코드.
 */
@Builder
public record ErrorResponse(
        String message,
        int status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
        ) {

    /**
     * 도메인 ErrorCode 기반 응답 팩토리 메서드.
     */
    public static ErrorResponse of(ErrorCode errorCode){
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatusCode())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 상태코드/메시지를 직접 지정하는 팩토리 메서드.
     */
    public static ErrorResponse of(int statusCode, String message){
        return ErrorResponse.builder()
                .message(message)
                .status(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }


}
