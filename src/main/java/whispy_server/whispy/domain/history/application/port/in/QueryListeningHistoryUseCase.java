package whispy_server.whispy.domain.history.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface QueryListeningHistoryUseCase {
    Page<ListeningHistoryResponse> execute(Pageable pageable);
}
