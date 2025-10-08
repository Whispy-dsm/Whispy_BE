package whispy_server.whispy.domain.history.adapter.out.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.history.adapter.out.entity.QListeningHistoryJpaEntity;
import whispy_server.whispy.domain.history.adapter.out.mapper.ListeningHistoryMapper;
import whispy_server.whispy.domain.history.adapter.out.persistence.repository.ListeningHistoryRepository;
import whispy_server.whispy.domain.history.application.port.out.ListeningHistoryPort;
import whispy_server.whispy.domain.history.model.ListeningHistory;
import whispy_server.whispy.domain.music.adapter.out.entity.QMusicJpaEntity;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ListeningHistoryPersistenceAdapter implements ListeningHistoryPort {

    private final ListeningHistoryRepository listeningHistoryRepository;
    private final ListeningHistoryMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(ListeningHistory history) {
        listeningHistoryRepository.save(mapper.toEntity(history));
    }

    @Override
    public Page<ListeningHistoryWithMusicDto> findListeningHistoryWithMusicByUserId(Long userId, Pageable pageable) {
        QListeningHistoryJpaEntity history = QListeningHistoryJpaEntity.listeningHistoryJpaEntity;
        QMusicJpaEntity music = QMusicJpaEntity.musicJpaEntity;

        List<ListeningHistoryWithMusicDto> content = jpaQueryFactory
                .select(Projections.constructor(
                        ListeningHistoryWithMusicDto.class,
                        music.id,
                        music.title,
                        music.filePath,
                        music.duration,
                        music.category,
                        history.listenedAt
                ))
                .from(history)
                .innerJoin(music).on(history.musicId.eq(music.id))
                .where(history.userId.eq(userId))
                .orderBy(history.listenedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(history.count())
                .from(history)
                .where(history.userId.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Optional<ListeningHistory> findByUserIdAndMusicId(Long userId, Long musicId) {
        return mapper.toOptionalModel(listeningHistoryRepository.findByUserIdAndMusicId(userId, musicId));
    }

    @Override
    public void deleteAllByMusicId(Long musicId) {
        listeningHistoryRepository.deleteAllByMusicId(musicId);
    }
}
