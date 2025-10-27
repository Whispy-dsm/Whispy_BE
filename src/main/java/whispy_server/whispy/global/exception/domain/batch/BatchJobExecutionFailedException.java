package whispy_server.whispy.global.exception.domain.batch;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class BatchJobExecutionFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new BatchJobExecutionFailedException();

    public BatchJobExecutionFailedException() {
        super(ErrorCode.BATCH_JOB_EXECUTION_ϜAILED);
    }
}
