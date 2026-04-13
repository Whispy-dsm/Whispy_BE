package whispy_server.whispy.global.exception.domain.statistics;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * ErrorCode.UNSUPPORTED_STATISTICS_CACHE_TARGET 상황을 나타내는 도메인 예외.
 */
public class UnsupportedStatisticsCacheTargetException extends WhispyException {

    public static final WhispyException EXCEPTION = new UnsupportedStatisticsCacheTargetException();

    private UnsupportedStatisticsCacheTargetException() {
        super(ErrorCode.UNSUPPORTED_STATISTICS_CACHE_TARGET);
    }
}
