package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.domain.meditationsession.application.port.in.GetMeditationSessionDetailUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.QueryMeditationSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.meditationsession.MeditationSessionNotFoundException;

/**
 * 명상 세션 상세 조회 서비스.
 *
 * 특정 명상 세션의 상세 정보를 조회하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeditationSessionDetailService implements GetMeditationSessionDetailUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 명상 세션의 상세 정보를 조회합니다.
     *
     * @param meditationSessionId 조회할 명상 세션 ID
     * @return 명상 세션의 상세 정보를 포함한 응답 DTO
     * @throws MeditationSessionNotFoundException 해당 ID의 세션이 존재하지 않거나 접근 권한이 없을 경우
     */
    @UserAction("명상 세션 상세 조회")
    @Override
    public MeditationSessionDetailResponse execute(Long meditationSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryMeditationSessionPort.findByIdAndUserId(userId, meditationSessionId)
                .map(MeditationSessionDetailResponse::from)
                .orElseThrow(() -> MeditationSessionNotFoundException.EXCEPTION);
    }
}
