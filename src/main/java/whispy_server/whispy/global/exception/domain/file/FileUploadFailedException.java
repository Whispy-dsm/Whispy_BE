package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.FILE_UPLOAD_FAILED 상황을 나타내는 도메인 예외.
 */
public class FileUploadFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new FileUploadFailedException();

    public FileUploadFailedException() {
        super(ErrorCode.FILE_UPLOAD_FAILED);
    }
}
