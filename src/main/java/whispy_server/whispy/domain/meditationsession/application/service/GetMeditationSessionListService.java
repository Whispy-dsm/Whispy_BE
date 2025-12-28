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
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 명상 세션 목록 조회 서비스.
 *
 * 현재 사용자의 명상 세션을 페이지 단위로 조회하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeditationSessionListService implements GetMeditationSessionListUseCase {

    private final QueryMeditationSessionPort queryMeditationSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 사용자의 명상 세션 목록을 페이지 단위로 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @return 명상 세션 목록 응답 DTO의 페이지
     */
    @UserAction("명상 세션 목록 조회")
    @Override
    public Page<MeditationSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryMeditationSessionPort.findByUserId(userId, pageable)
                .map(MeditationSessionListResponse::from);
    }
}
