package whispy_server.whispy.domain.focussession.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionListResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetFocusSessionListUseCase {
    Page<FocusSessionListResponse> execute(Pageable pageable);
}
