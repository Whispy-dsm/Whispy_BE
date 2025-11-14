package whispy_server.whispy.domain.announcement.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;

import java.util.List;

public interface QueryAllAnnouncementUseCase {
    Page<QueryAllAnnouncementResponse> execute(Pageable pageable);
}
