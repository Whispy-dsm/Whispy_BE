package whispy_server.whispy.domain.announcement.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.announcement.adapter.out.mapper.AnnouncementMapper;
import whispy_server.whispy.domain.announcement.adapter.out.persistence.repository.AnnouncementJpaRepository;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AnnouncementPersistenceAdapter implements AnnouncementPort {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementMapper announcementMapper;

    @Override
    public Announcement save(Announcement announcement) {
        return announcementMapper.toModel(
                announcementJpaRepository.save(announcementMapper.toEntity(announcement))
        );
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        return announcementMapper.toOptionalModel(announcementJpaRepository.findById(id));
    }

    @Override
    public Page<Announcement> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        return announcementMapper.toModelPage(announcementJpaRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    @Override
    public void deleteById(Long id) {
        announcementJpaRepository.deleteById(id);
    }
}
