package whispy_server.whispy.domain.soundspace.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.music.adapter.out.entity.QMusicJpaEntity;
import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;
import whispy_server.whispy.domain.soundspace.adapter.out.entity.QSoundSpaceMusicJpaEntity;
import whispy_server.whispy.domain.soundspace.adapter.out.mapper.SoundSpaceMusicMapper;
import whispy_server.whispy.domain.soundspace.adapter.out.persistence.repository.SoundSpaceMusicJpaRepository;
import whispy_server.whispy.domain.soundspace.application.port.out.SoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SoundSpaceMusicPersistenceAdapter implements SoundSpaceMusicPort {

    private final SoundSpaceMusicJpaRepository soundSpaceMusicJpaRepository;
    private final SoundSpaceMusicMapper mapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(SoundSpaceMusic soundSpaceMusic) {
        soundSpaceMusicJpaRepository.save(mapper.toEntity(soundSpaceMusic));
    }

    @Override
    public boolean existsByUserIdAndMusicId(Long userId, Long musicId) {
        return soundSpaceMusicJpaRepository.existsByUserIdAndMusicId(userId, musicId);
    }

    @Override
    public void deleteByUserIdAndMusicId(Long userId, Long musicId) {
        soundSpaceMusicJpaRepository.deleteByUserIdAndMusicId(userId, musicId);
    }

    @Override
    public List<SoundSpaceMusicWithDetailDto> findSoundSpaceMusicsWithDetailByUserId(Long userId) {
        QMusicJpaEntity music = QMusicJpaEntity.musicJpaEntity;
        QSoundSpaceMusicJpaEntity soundSpaceMusic = QSoundSpaceMusicJpaEntity.soundSpaceMusicJpaEntity;

        return queryFactory
                .select(Projections.constructor(
                        SoundSpaceMusicWithDetailDto.class,
                        music.id,
                        music.title,
                        music.filePath,
                        music.duration,
                        music.category
                ))
                .from(soundSpaceMusic)
                .innerJoin(music).on(soundSpaceMusic.musicId.eq(music.id))
                .where(soundSpaceMusic.userId.eq(userId))
                .orderBy(soundSpaceMusic.addedAt.desc())
                .fetch();
    }
    @Override
    public void deleteAllByMusicId(Long musicId) {
        soundSpaceMusicJpaRepository.deleteAllByMusicId(musicId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        soundSpaceMusicJpaRepository.deleteByUserId(userId);
    }
}
