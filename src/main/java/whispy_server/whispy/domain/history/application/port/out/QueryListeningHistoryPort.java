package whispy_server.whispy.domain.history.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.history.model.ListeningHistory;
import java.util.Optional;

/**
 * 청취 이력 조회 Port.
 */
public interface QueryListeningHistoryPort {
    /**
     * 사용자별 청취 이력을 음악 정보와 함께 조회한다.
     */
    Page<ListeningHistoryWithMusicDto> findListeningHistoryWithMusicByUserId(Long userId, Pageable pageable);

    /**
     * 사용자/음악 조합으로 청취 이력을 조회한다.
     */
    Optional<ListeningHistory> findByUserIdAndMusicId(Long userId, Long musicId);
}
