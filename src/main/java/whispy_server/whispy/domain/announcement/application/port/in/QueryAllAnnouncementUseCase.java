package whispy_server.whispy.domain.announcement.application.port.in;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;

import java.util.List;

public interface QueryAllAnnouncementUseCase {
    List<QueryAllAnnouncementResponse> execute();
}
