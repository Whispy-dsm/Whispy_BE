package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.application.port.in.DeleteMeditationSessionUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.DeleteMeditationSessionPort;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionNotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteMeditationSessionService implements DeleteMeditationSessionUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final DeleteMeditationSessionPort deleteMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional
    @Override
    public void execute(Long meditationSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        queryMeditationSessionPort.findByIdAndUserId(meditationSessionId, userId)
                .orElseThrow(() -> MeditationSessionNotFoundException.EXCEPTION);

        deleteMeditationSessionPort.deleteById(meditationSessionId);
    }
}
