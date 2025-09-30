package whispy_server.whispy.domain.announcement.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;
import whispy_server.whispy.domain.announcement.adapter.out.mapper.AnnouncementMapper;
import whispy_server.whispy.domain.announcement.adapter.out.persistence.repository.AnnouncementJpaRepository;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AnnouncementPersistenceAdapter implements AnnouncementPort {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementMapper announcementMapper;

    @Override
    public void save(Announcement announcement) {
        announcementJpaRepository.save(announcementMapper.toEntity(announcement));
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        return announcementMapper.toOptionalModel(announcementJpaRepository.findById(id));
    }

    @Override
    public List<Announcement> findAll() {
        return announcementMapper.toModelList(announcementJpaRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        announcementJpaRepository.deleteById(id);
    }
}
