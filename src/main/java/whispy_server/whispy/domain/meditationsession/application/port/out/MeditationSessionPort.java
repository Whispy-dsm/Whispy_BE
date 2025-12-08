package whispy_server.whispy.domain.meditationsession.application.port.out;

/**
 * 명상 세션 통합 포트 인터페이스.
 *
 * 명상 세션의 저장, 조회, 삭제 작업을 모두 포함하는 아웃바운드 포트입니다.
 */
public interface MeditationSessionPort extends MeditationSessionSavePort, QueryMeditationSessionPort, DeleteMeditationSessionPort {
}
