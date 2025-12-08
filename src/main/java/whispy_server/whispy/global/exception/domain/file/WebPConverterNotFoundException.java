package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.WEBP_CONVERTER_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class WebPConverterNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new WebPConverterNotFoundException();

    public WebPConverterNotFoundException() {
        super(ErrorCode.WEBP_CONVERTER_NOT_FOUND);
    }
}
