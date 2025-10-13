package whispy_server.whispy.domain.announcement.application.port.out;

import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface QueryAnnouncementPort {
    Optional<Announcement> findById(Long id);
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
