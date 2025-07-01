package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileInvalidExtensionException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileInvalidExtensionException();

    public FileInvalidExtensionException() {
        super(ErrorCode.FILE_INVALID_EXTENSION);
    }
}
