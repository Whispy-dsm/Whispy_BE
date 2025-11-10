package whispy_server.whispy.domain.reason.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.util.Optional;

public interface WithdrawalReasonQueryPort {
    Page<WithdrawalReason> findAll(Pageable pageable);
    Optional<WithdrawalReason> findById(Long id);
    boolean existsById(Long id);
}
