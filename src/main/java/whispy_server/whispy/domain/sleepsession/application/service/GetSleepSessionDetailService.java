package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionDetailUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionNotFoundException;

/**
 * 수면 세션 상세 조회 서비스.
 *
 * 특정 수면 세션의 상세 정보를 조회하는 유스케이스 구현입니다.
 * 현재 사용자의 세션만 조회 가능합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSleepSessionDetailService implements GetSleepSessionDetailUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 특정 수면 세션의 상세 정보를 조회합니다.
     *
     * @param sleepSessionId 조회할 수면 세션 ID
     * @return 수면 세션 상세 정보
     * @throws SleepSessionNotFoundException 해당 ID의 세션이 없거나 현재 사용자의 세션이 아닐 경우
     */
    @Override
    public SleepSessionDetailResponse execute(Long sleepSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return querySleepSessionPort.findByIdAndUserId(sleepSessionId, userId)
                .map(SleepSessionDetailResponse::from)
                .orElseThrow(() -> SleepSessionNotFoundException.EXCEPTION);
    }
}
