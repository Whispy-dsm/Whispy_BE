package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

@Service
@RequiredArgsConstructor
public class QueryAnnouncementService implements QueryAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    @Transactional(readOnly = true)
    @Override
    public QueryAnnouncementResponse execute(Long id) {
        Announcement announcement = announcementPort.findById(id)
                .orElseThrow(() -> AnnouncementNotFoundException.EXCEPTION);
        
        return new QueryAnnouncementResponse(
                announcement.title(),
                announcement.content(),
                announcement.bannerImageUrl()
        );
    }
}
