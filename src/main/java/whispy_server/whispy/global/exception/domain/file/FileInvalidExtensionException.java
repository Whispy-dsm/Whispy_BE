package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FILE_INVALID_EXTENSION 상황을 나타내는 도메인 예외.
 */
public class FileInvalidExtensionException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileInvalidExtensionException();

    public FileInvalidExtensionException() {
        super(ErrorCode.FILE_INVALID_EXTENSION);
    }
}
