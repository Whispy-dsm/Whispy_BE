package whispy_server.whispy.domain.reason.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.util.Optional;

/**
 * 탈퇴 사유 조회 Port.
 */
public interface WithdrawalReasonQueryPort {
    /**
     * 탈퇴 사유 목록을 페이지로 조회한다.
     */
    Page<WithdrawalReason> findAll(Pageable pageable);

    /**
     * ID로 탈퇴 사유를 조회한다.
     */
    Optional<WithdrawalReason> findById(Long id);

    /**
     * ID 존재 여부를 확인한다.
     */
    boolean existsById(Long id);
}
