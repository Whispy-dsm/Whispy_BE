package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileNameTooLongException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNameTooLongException();

    public FileNameTooLongException() {
        super(ErrorCode.FILE_NAME_TOO_LONG);
    }
}
