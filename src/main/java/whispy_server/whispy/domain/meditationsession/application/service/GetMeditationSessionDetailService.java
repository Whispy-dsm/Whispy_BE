package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.domain.meditationsession.application.port.in.GetMeditationSessionDetailUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeditationSessionDetailService implements GetMeditationSessionDetailUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public MeditationSessionDetailResponse execute(Long meditationSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryMeditationSessionPort.findByIdAndUserId(userId, meditationSessionId)
                .map(MeditationSessionDetailResponse::from)
                .orElseThrow(() -> MeditationSessionNotFoundException.EXCEPTION);
    }
}
