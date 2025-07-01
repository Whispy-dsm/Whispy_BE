package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileSizeExceededException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileSizeExceededException();

    public FileSizeExceededException() {
        super(ErrorCode.FILE_SIZE_EXCEEDED);
    }
}
