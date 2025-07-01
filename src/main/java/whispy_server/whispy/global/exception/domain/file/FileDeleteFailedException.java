package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileDeleteFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileDeleteFailedException();

    public FileDeleteFailedException() {
        super(ErrorCode.FILE_DELETE_FAILED);
    }
}
