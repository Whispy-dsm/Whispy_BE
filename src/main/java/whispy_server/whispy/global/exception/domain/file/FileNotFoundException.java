package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FILE_NOT_FOUND 상황을 나타내는 도메인 예외.
 */
public class FileNotFoundException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNotFoundException();

    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
