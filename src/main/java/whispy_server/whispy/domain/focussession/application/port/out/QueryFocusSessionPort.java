package whispy_server.whispy.domain.focussession.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.focussession.model.FocusSession;

import java.util.Optional;

public interface QueryFocusSessionPort {
    Page<FocusSession> findByUserId(Long userId, Pageable pageable);
    Optional<FocusSession> findByIdAndUserId(Long id, Long userId);
}
