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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSleepSessionListService implements GetSleepSessionListUseCase {

    private final QuerySleepSessionPort querySleepSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public Page<SleepSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return querySleepSessionPort.findByUserId(userId, pageable)
                .map(SleepSessionListResponse::from);
    }
}
