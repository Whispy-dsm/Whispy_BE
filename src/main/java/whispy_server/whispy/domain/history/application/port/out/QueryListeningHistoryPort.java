package whispy_server.whispy.domain.history.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.history.model.ListeningHistory;
import java.util.Optional;

public interface QueryListeningHistoryPort {
    Page<ListeningHistoryWithMusicDto> findListeningHistoryWithMusicByUserId(Long userId, Pageable pageable);
    Optional<ListeningHistory> findByUserIdAndMusicId(Long userId, Long musicId);
}
