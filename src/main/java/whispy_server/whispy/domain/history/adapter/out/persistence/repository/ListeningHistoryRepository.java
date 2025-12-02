package whispy_server.whispy.domain.history.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.history.adapter.out.entity.ListeningHistoryJpaEntity;
import java.util.Optional;

/**
 * 청취 이력용 JPA 리포지토리.
 */
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistoryJpaEntity, Long> {

    /**
     * 사용자/음악 조합으로 이력을 조회한다.
     */
    Optional<ListeningHistoryJpaEntity> findByUserIdAndMusicId(Long userId, Long musicId);

    /**
     * 음악 기준 청취 이력을 삭제한다.
     */
    void deleteAllByMusicId(Long musicId);

    /**
     * 사용자 기준 청취 이력을 삭제한다.
     */
    void deleteByUserId(Long userId);
}
