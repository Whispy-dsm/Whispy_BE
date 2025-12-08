package whispy_server.whispy.global.exception.domain.statistics;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.INVALID_STATISTICS_DATE 상황을 나타내는 도메인 예외.
 */
public class InvalidStatisticsDateException extends WhispyException {

    public static final WhispyException EXCEPTION = new InvalidStatisticsDateException();

    private InvalidStatisticsDateException() {
        super(ErrorCode.INVALID_STATISTICS_DATE);
    }
}
