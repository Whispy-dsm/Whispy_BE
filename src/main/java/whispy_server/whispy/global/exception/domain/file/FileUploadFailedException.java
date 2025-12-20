package whispy_server.whispy.global.exception.domain.file;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 파일 업로드 실패 예외.
 *
 * 파일 시스템 저장 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class FileUploadFailedException extends WhispyException {

    /**
     * 원인 예외와 함께 파일 업로드 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public FileUploadFailedException(Throwable cause) {
        super(ErrorCode.FILE_UPLOAD_FAILED, cause);
    }
}
