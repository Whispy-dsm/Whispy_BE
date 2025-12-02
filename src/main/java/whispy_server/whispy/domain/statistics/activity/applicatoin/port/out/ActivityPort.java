package whispy_server.whispy.domain.statistics.activity.applicatoin.port.out;

/**
 * 활동 통계 아웃바운드 포트.
 *
 * 활동 통계 조회를 위한 아웃바운드 포트 인터페이스입니다.
 */
public interface ActivityPort extends CheckWeeklySessionExistsPort, QueryActivityMinutesPort{
}
