package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.UpdateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

@Service
@RequiredArgsConstructor
public class UpdateAnnouncementService implements UpdateAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    @Transactional
    @Override
    public void execute(UpdateAnnouncementRequest request) {
        Announcement announcement = announcementPort.findById(request.id())
                .orElseThrow(() -> AnnouncementNotFoundException.EXCEPTION);
        
        Announcement updatedAnnouncement = announcement.update(request);
        
        announcementPort.save(updatedAnnouncement);
    }
}
