package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class ImageInvalidException extends WhispyException {

    public static final WhispyException EXCEPTION = new ImageInvalidException();

    public ImageInvalidException() {
        super(ErrorCode.IMAGE_INVALID);
    }
}
