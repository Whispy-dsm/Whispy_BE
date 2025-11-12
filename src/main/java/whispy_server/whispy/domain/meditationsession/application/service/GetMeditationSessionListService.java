package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.domain.meditationsession.application.port.in.GetMeditationSessionListUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeditationSessionListService implements GetMeditationSessionListUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public Page<MeditationSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryMeditationSessionPort.findByUserId(userId, pageable)
                .map(MeditationSessionListResponse::from);
    }
}
