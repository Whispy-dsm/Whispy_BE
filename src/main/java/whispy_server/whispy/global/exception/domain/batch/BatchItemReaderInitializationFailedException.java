package whispy_server.whispy.global.exception.domain.batch;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 배치 ItemReader 초기화 실패 예외.
 *
 * Spring Batch의 ItemReader 초기화 과정에서 발생하는 예외를 래핑합니다.
 */
public class BatchItemReaderInitializationFailedException extends WhispyException {

    public static final WhispyException EXCEPTION = new BatchItemReaderInitializationFailedException();

    public BatchItemReaderInitializationFailedException() {
        super(ErrorCode.BATCH_ITEM_READER_INITIALIZATION_FAILED);
    }
}
