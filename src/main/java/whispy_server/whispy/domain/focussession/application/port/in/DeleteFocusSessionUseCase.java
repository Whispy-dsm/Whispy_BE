package whispy_server.whispy.domain.focussession.application.port.in;

/**
 * 집중 세션 삭제 유스케이스 인터페이스.
 *
 * 특정 집중 세션을 삭제하는 애플리케이션 작업을 정의합니다.
 */
public interface DeleteFocusSessionUseCase {

    /**
     * 특정 집중 세션을 삭제합니다.
     *
     * @param focusSessionId 삭제할 집중 세션 ID
     * @throws FocusSessionNotFoundException 해당 ID의 세션이 존재하지 않을 경우
     */
    void execute(Long focusSessionId);
}
