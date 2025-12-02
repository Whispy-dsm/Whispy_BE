package whispy_server.whispy.domain.meditationsession.application.port.out;

/**
 * 명상 세션 삭제 포트 인터페이스.
 *
 * 명상 세션 삭제 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface DeleteMeditationSessionPort {

    /**
     * 세션 ID로 명상 세션을 삭제합니다.
     *
     * @param id 삭제할 세션 ID
     */
    void deleteById(Long id);

    /**
     * 사용자 ID로 해당 사용자의 모든 명상 세션을 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID
     */
    void deleteByUserId(Long userId);
}
