package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionListResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionListUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

/**
 * 수면 세션 목록 조회 서비스.
 *
 * 현재 사용자의 수면 세션 목록을 페이지 단위로 조회하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSleepSessionListService implements GetSleepSessionListUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 현재 사용자의 수면 세션 목록을 페이지 단위로 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 수면 세션 목록 페이지
     */
    @Override
    public Page<SleepSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return querySleepSessionPort.findByUserId(userId, pageable)
                .map(SleepSessionListResponse::from);
    }
}
