package whispy_server.whispy.global.exception.domain.statistics;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.DATE_RANGE_EXCEEDED 상황을 나타내는 도메인 예외.
 */
public class DateRangeExceededException extends WhispyException {

    public static final WhispyException EXCEPTION = new DateRangeExceededException();

    private DateRangeExceededException() {
        super(ErrorCode.DATE_RANGE_EXCEEDED);
    }
}
