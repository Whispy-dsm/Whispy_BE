package whispy_server.whispy.domain.meditationsession.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;

import java.util.Optional;

public interface QueryMeditationSessionPort {
    Page<MeditationSession> findByUserId(Long userId, Pageable pageable);
    Optional<MeditationSession> findByIdAndUserId(Long id, Long userId);
}
