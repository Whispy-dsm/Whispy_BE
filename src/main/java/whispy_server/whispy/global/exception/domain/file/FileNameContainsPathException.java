package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FILE_NAME_CONTAINS_PATH 상황을 나타내는 도메인 예외.
 */
public class FileNameContainsPathException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileNameContainsPathException();

    public FileNameContainsPathException() {
        super(ErrorCode.FILE_NAME_CONTAINS_PATH);
    }
}
