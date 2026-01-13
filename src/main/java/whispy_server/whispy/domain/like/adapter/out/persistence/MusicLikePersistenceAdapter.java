package whispy_server.whispy.domain.like.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.like.adapter.out.dto.MusicLikeWithMusicDto;
import whispy_server.whispy.domain.like.adapter.out.entity.QMusicLikeJpaEntity;
import whispy_server.whispy.domain.like.adapter.out.mapper.MusicLikeMapper;
import whispy_server.whispy.domain.like.adapter.out.persistence.repository.MusicLikeJpaRepository;
import whispy_server.whispy.domain.like.application.port.out.MusicLikePort;
import whispy_server.whispy.domain.like.model.MusicLike;
import whispy_server.whispy.domain.music.adapter.out.entity.QMusicJpaEntity;

import java.util.List;
import java.util.Optional;

/**
 * 좋아요 도메인의 영속 계층 어댑터로, JPA/QueryDSL 을 통해 CRUD 를 수행한다.
 */
@Component
@RequiredArgsConstructor
public class MusicLikePersistenceAdapter implements MusicLikePort {

    private final MusicLikeJpaRepository musicLikeJpaRepository;
    private final MusicLikeMapper musicLikeMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(MusicLike musicLike) {
        musicLikeJpaRepository.save(musicLikeMapper.toEntity(musicLike));
    }

    @Override
    public boolean existsByUserIdAndMusicId(Long userId, Long musicId) {
        return musicLikeJpaRepository.existsByUserIdAndMusicId(userId, musicId);
    }

    @Override
    public void deleteByUserIdAndMusicId(Long userId, Long musicId) {
        musicLikeJpaRepository.deleteByUserIdAndMusicId(userId, musicId);
    }

    @Override
    public void deleteAllByMusicId(Long musicId) {
        musicLikeJpaRepository.deleteAllByMusicId(musicId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        musicLikeJpaRepository.deleteByUserId(userId);
    }

    @Override
    public List<MusicLike> findAllByUserId(Long userId) {
        return musicLikeMapper.toModelList(musicLikeJpaRepository.findAllByUserId(userId));
    }

    @Override
    public Optional<MusicLike> findByUserIdAndMusicId(Long userId, Long musicId) {
        return musicLikeMapper.toOptionalModel(musicLikeJpaRepository.findByUserIdAndMusicId(userId, musicId));
    }

    @Override
    public List<MusicLikeWithMusicDto> findLikedMusicsWithDetailByUserId(Long userId) {
        QMusicLikeJpaEntity musicLike = QMusicLikeJpaEntity.musicLikeJpaEntity;
        QMusicJpaEntity music = QMusicJpaEntity.musicJpaEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        MusicLikeWithMusicDto.class,
                        music.id,
                        music.title,
                        music.filePath,
                        music.duration,
                        music.category,
                        music.bannerImageUrl
                ))
                .from(musicLike)
                .innerJoin(music).on(musicLike.musicId.eq(music.id))
                .where(musicLike.userId.eq(userId))
                .fetch();
    }
}
