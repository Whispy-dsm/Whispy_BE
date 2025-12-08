package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionListResponse;
import whispy_server.whispy.domain.focussession.application.port.in.GetFocusSessionListUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.QueryFocusSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

/**
 * 집중 세션 목록 조회 서비스.
 *
 * 현재 사용자의 집중 세션 목록을 페이지 단위로 조회하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetFocusSessionListService implements GetFocusSessionListUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 사용자의 집중 세션 목록을 페이지 단위로 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 집중 세션 목록 페이지
     */
    @Override
    public Page<FocusSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryFocusSessionPort.findByUserId(userId, pageable)
                .map(FocusSessionListResponse::from);
    }
}
