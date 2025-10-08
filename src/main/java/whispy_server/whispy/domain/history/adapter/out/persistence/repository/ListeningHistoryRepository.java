package whispy_server.whispy.domain.history.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.history.adapter.out.entity.ListeningHistoryJpaEntity;
import java.util.Optional;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistoryJpaEntity, Long> {

    Optional<ListeningHistoryJpaEntity> findByUserIdAndMusicId(Long userId, Long musicId);
    void deleteAllByMusicId(Long musicId);
}
