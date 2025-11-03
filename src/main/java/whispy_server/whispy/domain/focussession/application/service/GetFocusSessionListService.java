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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetFocusSessionListService implements GetFocusSessionListUseCase {

    private final QueryFocusSessionPort queryFocusSessionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public Page<FocusSessionListResponse> execute(Pageable pageable) {
        Long userId = userFacadeUseCase.currentUser().id();
        return queryFocusSessionPort.findByUserId(userId, pageable)
                .map(FocusSessionListResponse::from);
    }
}
