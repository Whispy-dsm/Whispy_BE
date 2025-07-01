package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileNoExtensionException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNoExtensionException();

    public FileNoExtensionException() {
        super(ErrorCode.FILE_NO_EXTENSION);
    }
}
