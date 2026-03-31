package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 파일 조회 실패 예외.
 */
public class FileReadFailedException extends WhispyException {

    public FileReadFailedException(Throwable cause) {
        super(ErrorCode.FILE_READ_FAILED, cause);
    }
}
