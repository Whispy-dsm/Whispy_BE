package whispy_server.whispy.domain.sleepsession.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.util.Optional;

public interface QuerySleepSessionPort {
    Page<SleepSession> findByUserId(Long userId, Pageable pageable);
    Optional<SleepSession> findByIdAndUserId(Long id, Long userId);
}
