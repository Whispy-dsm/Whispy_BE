package whispy_server.whispy.domain.history.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.domain.history.application.port.in.QueryListeningHistoryUseCase;
import whispy_server.whispy.domain.history.application.port.out.QueryListeningHistoryPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryListeningHistoryService implements QueryListeningHistoryUseCase {

    private final QueryListeningHistoryPort queryListeningHistoryPort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public Page<ListeningHistoryResponse> execute(Pageable pageable) {
        User currentUser = userFacadeUseCase.currentUser();

        return queryListeningHistoryPort.findListeningHistoryWithMusicByUserId(currentUser.id(), pageable)
                .map(ListeningHistoryResponse::from);
    }
}
