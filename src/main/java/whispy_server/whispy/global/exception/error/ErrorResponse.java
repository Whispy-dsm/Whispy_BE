package whispy_server.whispy.global.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        int status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        String description,
        String exception
        ) {

    public static ErrorResponse of(ErrorCode errorCode, String description, Exception e){
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatusCode())
                .timestamp(LocalDateTime.now())
                .description(description)
                .exception(e.getClass().getSimpleName())
                .build();
    }

    public static ErrorResponse of(int statusCode, String description, Exception e){
        return ErrorResponse.builder()
                .message(description)
                .status(statusCode)
                .timestamp(LocalDateTime.now())
                .description(description)
                .exception(e.getClass().getSimpleName())
                .build();
    }


}
