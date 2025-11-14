package whispy_server.whispy.domain.announcement.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface QueryAnnouncementPort {
    Optional<Announcement> findById(Long id);
    Page<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
