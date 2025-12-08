package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.application.port.in.DeleteMeditationSessionUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.DeleteMeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionNotFoundException;

/**
 * 명상 세션 삭제 서비스.
 *
 * 명상 세션을 삭제하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
public class DeleteMeditationSessionService implements DeleteMeditationSessionUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final DeleteMeditationSessionPort deleteMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 명상 세션을 삭제합니다.
     *
     * @param meditationSessionId 삭제할 명상 세션 ID
     * @throws MeditationSessionNotFoundException 해당 ID의 세션이 존재하지 않거나 접근 권한이 없을 경우
     */
    @Transactional
    @Override
    public void execute(Long meditationSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        queryMeditationSessionPort.findByIdAndUserId(meditationSessionId, userId)
                .orElseThrow(() -> MeditationSessionNotFoundException.EXCEPTION);

        deleteMeditationSessionPort.deleteById(meditationSessionId);
    }
}
