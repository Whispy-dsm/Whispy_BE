package whispy_server.whispy.domain.statistics.common.constants;

import java.time.LocalTime;

public final class TimeConstants {

    private TimeConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);
    
    public static final int SECONDS_PER_MINUTE = 60;
    
    public static final int HOURS_PER_DAY = 24;
    public static final int FIRST_HOUR_OF_DAY = 0;
    
    public static final int MONTHS_PER_YEAR = 12;
    public static final int FIRST_MONTH_OF_YEAR = 1;
    
    public static final int MINUTES_PER_HOUR = 60;
}
