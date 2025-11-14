package whispy_server.whispy.domain.announcement.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAllAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAnnouncementUseCase;
import whispy_server.whispy.global.document.api.announcement.AnnouncementApiDocument;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementApiDocument {

    private final QueryAnnouncementUseCase queryAnnouncementUseCase;
    private final QueryAllAnnouncementUseCase queryAllAnnouncementUseCase;

    @GetMapping("/{id}")
    @Override
    public QueryAnnouncementResponse getAnnouncement(@PathVariable Long id) {
        return queryAnnouncementUseCase.execute(id);
    }

    @GetMapping
    @Override
    public Page<QueryAllAnnouncementResponse> getAllAnnouncements(Pageable pageable) {
        return queryAllAnnouncementUseCase.execute(pageable);
    }
}
