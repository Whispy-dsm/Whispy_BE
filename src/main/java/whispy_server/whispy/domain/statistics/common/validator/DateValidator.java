package whispy_server.whispy.domain.statistics.common.validator;

import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;

public final class DateValidator {

    private DateValidator() {
        throw new AssertionError("유틸리티 클래스는 객체를 생성하지 않도록 설계해야 합니다.");
    }

    public static void validateNotFutureDate(LocalDate date) {
        if (date == null) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
        
        if (date.isAfter(LocalDate.now())) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
    }
}
