package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FILE_INVALID_MIME_TYPE 상황을 나타내는 도메인 예외.
 */
public class FileInvalidMimeTypeException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileInvalidMimeTypeException();

    public FileInvalidMimeTypeException() {
        super(ErrorCode.FILE_INVALID_MIME_TYPE);
    }
}
