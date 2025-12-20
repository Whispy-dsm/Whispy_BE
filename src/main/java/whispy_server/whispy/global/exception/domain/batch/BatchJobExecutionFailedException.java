package whispy_server.whispy.global.exception.domain.batch;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 배치 작업 실행 실패 예외.
 *
 * Spring Batch Job 실행 과정에서 발생하는 예외를 래핑합니다.
 * 원인 예외를 포함하여 정확한 스택 트레이스를 보존합니다.
 */
public class BatchJobExecutionFailedException extends WhispyException {

    /**
     * 원인 예외와 함께 배치 작업 실행 실패 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public BatchJobExecutionFailedException(Throwable cause) {
        super(ErrorCode.BATCH_JOB_EXECUTION_FAILED, cause);
    }
}
