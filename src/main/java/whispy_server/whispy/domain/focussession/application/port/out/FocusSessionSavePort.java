package whispy_server.whispy.domain.focussession.application.port.out;

import whispy_server.whispy.domain.focussession.model.FocusSession;

/**
 * 집중 세션 저장 포트 인터페이스.
 *
 * 집중 세션 저장 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface FocusSessionSavePort {

    /**
     * 집중 세션을 저장합니다.
     *
     * @param session 저장할 도메인 모델
     * @return 저장된 세션의 도메인 모델 (ID 포함)
     */
    FocusSession save(FocusSession session);
}
