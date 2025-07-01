package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class FileNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNotFoundException();

    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
