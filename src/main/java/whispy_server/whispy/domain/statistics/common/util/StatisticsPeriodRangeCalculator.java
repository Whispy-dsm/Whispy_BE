package whispy_server.whispy.domain.statistics.common.util;

import whispy_server.whispy.domain.statistics.common.constants.TimeConstants;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.domain.statistics.sleep.types.SleepPeriodType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 통계 기간 범위 계산 유틸리티.
 *
 * 주어진 기간 타입과 날짜를 바탕으로 시작 및 종료 시간을 계산합니다.
 */
public final class StatisticsPeriodRangeCalculator {

    private StatisticsPeriodRangeCalculator() {
        throw new AssertionError("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    /**
     * 집중 통계 기간 범위를 계산합니다.
     *
     * @param period 기간 타입
     * @param date 기준 날짜
     * @return [시작 시간, 종료 시간]을 포함한 배열
     */
    public static LocalDateTime[] calculateFocusPeriodRange(FocusPeriodType period, LocalDate date) {
        return switch (period) {
            case TODAY -> calculateDayRange(date);
            case WEEK -> calculateWeekRange(date);
            case MONTH -> calculateMonthRange(date);
            case YEAR -> calculateYearRange(date);
        };
    }

    /**
     * 수면 통계 기간 범위를 계산합니다.
     *
     * @param period 기간 타입
     * @param date 기준 날짜
     * @return [시작 시간, 종료 시간]을 포함한 배열
     */
    public static LocalDateTime[] calculateSleepPeriodRange(SleepPeriodType period, LocalDate date) {
        return switch (period) {
            case WEEK -> calculateWeekRange(date);
            case MONTH -> calculateMonthRange(date);
            case YEAR -> calculateYearRange(date);
        };
    }

    private static LocalDateTime[] calculateDayRange(LocalDate date) {
        return new LocalDateTime[]{
                date.atStartOfDay(),
                date.atTime(TimeConstants.END_OF_DAY)
        };
    }

    private static LocalDateTime[] calculateWeekRange(LocalDate date) {
        return new LocalDateTime[]{
                date.with(DayOfWeek.MONDAY).atStartOfDay(),
                date.with(DayOfWeek.SUNDAY).atTime(TimeConstants.END_OF_DAY)
        };
    }

    private static LocalDateTime[] calculateMonthRange(LocalDate date) {
        return new LocalDateTime[]{
                date.withDayOfMonth(1).atStartOfDay(),
                date.withDayOfMonth(date.lengthOfMonth()).atTime(TimeConstants.END_OF_DAY)
        };
    }

    private static LocalDateTime[] calculateYearRange(LocalDate date) {
        return new LocalDateTime[]{
                date.withDayOfYear(1).atStartOfDay(),
                date.withDayOfYear(date.lengthOfYear()).atTime(TimeConstants.END_OF_DAY)
        };
    }
}
