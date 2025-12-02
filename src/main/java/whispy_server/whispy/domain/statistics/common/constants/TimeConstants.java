package whispy_server.whispy.domain.statistics.common.constants;

import java.time.LocalTime;

/**
 * 통계 도메인에서 사용하는 시간 관련 상수.
 *
 * 시간 계산에 필요한 각종 시간 상수를 정의합니다.
 */
public final class TimeConstants {

    private TimeConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** 하루의 끝 시간(23:59:59) */
    public static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);

    /** 1분의 초 단위 */
    public static final int SECONDS_PER_MINUTE = 60;

    /** 하루의 시간 수 */
    public static final int HOURS_PER_DAY = 24;

    /** 하루의 시작 시간 */
    public static final int FIRST_HOUR_OF_DAY = 0;

    /** 1년의 월 수 */
    public static final int MONTHS_PER_YEAR = 12;

    /** 1년의 첫 번째 월 */
    public static final int FIRST_MONTH_OF_YEAR = 1;

    /** 1시간의 분 단위 */
    public static final int MINUTES_PER_HOUR = 60;
}
