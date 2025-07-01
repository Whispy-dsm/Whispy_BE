package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileNameEmptyException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNameEmptyException();

    public FileNameEmptyException() {
        super(ErrorCode.FILE_NAME_EMPTY);
    }
}
