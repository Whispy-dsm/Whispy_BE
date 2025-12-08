package whispy_server.whispy.domain.meditationsession.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 명상 세션 삭제 유스케이스 인터페이스.
 *
 * 특정 명상 세션을 삭제하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface DeleteMeditationSessionUseCase {

    /**
     * 특정 명상 세션을 삭제합니다.
     *
     * @param meditationSessionId 삭제할 명상 세션 ID
     * @throws MeditationSessionNotFoundException 해당 ID의 세션이 존재하지 않을 경우
     */
    void execute(Long meditationSessionId);
}
