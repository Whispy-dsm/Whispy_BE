package whispy_server.whispy.domain.announcement.application.port.out;

import whispy_server.whispy.domain.announcement.model.Announcement;

public interface SaveAnnouncementPort {
    Announcement save(Announcement announcement);
}
