package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionDetailUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.QuerySleepSessionPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.exception.domain.sleepsession.SleepSessionNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSleepSessionDetailService implements GetSleepSessionDetailUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public SleepSessionDetailResponse execute(Long sleepSessionId) {
        Long userId = userFacadeUseCase.currentUser().id();
        return querySleepSessionPort.findByIdAndUserId(sleepSessionId, userId)
                .map(SleepSessionDetailResponse::from)
                .orElseThrow(() -> SleepSessionNotFoundException.EXCEPTION);
    }
}
