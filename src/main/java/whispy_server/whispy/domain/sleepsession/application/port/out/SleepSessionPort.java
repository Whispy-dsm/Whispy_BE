package whispy_server.whispy.domain.sleepsession.application.port.out;

/**
 * 수면 세션 통합 포트 인터페이스.
 *
 * 수면 세션의 저장, 조회, 삭제 작업을 모두 포함하는 아웃바운드 포트입니다.
 */
public interface SleepSessionPort extends SleepSessionSavePort, QuerySleepSessionPort, DeleteSleepSessionPort {
}
