package whispy_server.whispy.global.exception.domain.statistics;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_DATE_RANGE 상황을 나타내는 도메인 예외.
 */
public class InvalidDateRangeException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidDateRangeException();

    private InvalidDateRangeException() {
        super(ErrorCode.INVALID_DATE_RANGE);
    }
}
