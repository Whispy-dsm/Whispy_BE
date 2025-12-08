package whispy_server.whispy.domain.statistics.common.validator;

import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;

/**
 * 통계 도메인의 날짜 검증 유틸리티.
 *
 * 통계 조회에 필요한 날짜 유효성을 검증합니다.
 */
public final class DateValidator {

    private DateValidator() {
        throw new AssertionError("유틸리티 클래스는 객체를 생성하지 않도록 설계해야 합니다.");
    }

    /**
     * 미래 날짜가 아님을 검증합니다.
     *
     * @param date 검증할 날짜
     * @throws InvalidStatisticsDateException 날짜가 null이거나 미래인 경우
     */
    public static void validateNotFutureDate(LocalDate date) {
        if (date == null) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
        
        if (date.isAfter(LocalDate.now())) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
    }
}
