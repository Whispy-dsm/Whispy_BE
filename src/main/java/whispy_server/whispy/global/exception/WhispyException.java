package whispy_server.whispy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import whispy_server.whispy.global.exception.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public class WhispyException extends RuntimeΕxception{

    private final ErrorCode errorCode;
}
