package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.domain.focussession.application.port.in.GetFocusSessionDetailUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.QueryFocusSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.focussession.FocusSessionNotFoundException;

/**
 * 집중 세션 상세 조회 서비스.
 *
 * 특정 집중 세션의 상세 정보를 조회하는 유스케이스 구현입니다.
 * 현재 사용자의 세션만 조회 가능합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetFocusSessionDetailService implements GetFocusSessionDetailUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 집중 세션의 상세 정보를 조회합니다.
     *
     * @param focusSessionId 조회할 집중 세션 ID
     * @return 집중 세션 상세 정보
     * @throws FocusSessionNotFoundException 해당 ID의 세션이 없거나 현재 사용자의 세션이 아닐 경우
     */
    @Override
    public FocusSessionDetailResponse execute(Long focusSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryFocusSessionPort.findByIdAndUserId(userId, focusSessionId)
                .map(FocusSessionDetailResponse::from)
                .orElseThrow(() -> FocusSessionNotFoundException.EXCEPTION);
    }
}
