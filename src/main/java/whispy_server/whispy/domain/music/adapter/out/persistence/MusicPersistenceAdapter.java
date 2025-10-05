package whispy_server.whispy.domain.music.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.adapter.out.mapper.MusicMapper;
import whispy_server.whispy.domain.music.adapter.out.persistence.repository.MusicJpaRepository;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MusicPersistenceAdapter implements MusicPort {

    private final MusicJpaRepository musicJpaRepository;
    private final MusicMapper musicMapper;

    @Override
    public Music save(Music music) {
        MusicJpaEntity entity = musicMapper.toEntity(music);
        MusicJpaEntity savedEntity = musicJpaRepository.save(entity);
        return musicMapper.toModel(savedEntity);
    }

    @Override
    public Optional<Music> findById(Long id) {
        return musicMapper.toOptionalModel(musicJpaRepository.findById(id));
    }

    @Override
    public boolean existsById(Long id) {
        return musicJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        musicJpaRepository.deleteById(id);
    }

    @Override
    public Page<Music> searchByTitle(String title, Pageable pageable) {
        return musicMapper.toPageModel(musicJpaRepository.findByTitleContaining(title, pageable));
    }

    @Override
    public Page<Music> searchByCategory(MusicCategory category, Pageable pageable) {
        return musicMapper.toPageModel(musicJpaRepository.findByCategory(category, pageable));
    }
}
