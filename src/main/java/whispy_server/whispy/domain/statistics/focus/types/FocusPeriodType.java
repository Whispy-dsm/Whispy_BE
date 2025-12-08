package whispy_server.whispy.domain.statistics.focus.types;

/**
 * 집중 통계 기간 타입.
 *
 * 집중 통계 조회시 사용하는 기간 단위입니다.
 */
public enum FocusPeriodType {
    /** 일일 통계 */
    TODAY,
    /** 주간 통계 */
    WEEK,
    /** 월간 통계 */
    MONTH,
    /** 연간 통계 */
    YEAR
}
