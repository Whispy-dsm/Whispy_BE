package whispy_server.whispy.domain.meditationsession.application.port.out;

import whispy_server.whispy.domain.meditationsession.model.MeditationSession;

/**
 * 명상 세션 저장 포트 인터페이스.
 *
 * 명상 세션 저장 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface MeditationSessionSavePort {

    /**
     * 명상 세션을 저장합니다.
     *
     * @param session 저장할 도메인 모델
     * @return 저장된 세션의 도메인 모델 (ID 포함)
     */
    MeditationSession save(MeditationSession session);
}
