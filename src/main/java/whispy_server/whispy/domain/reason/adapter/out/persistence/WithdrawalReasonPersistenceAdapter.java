package whispy_server.whispy.domain.reason.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.reason.adapter.out.mapper.WithdrawalReasonMapper;
import whispy_server.whispy.domain.reason.adapter.out.persistence.repository.WithdrawalReasonJpaRepository;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonPort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;

import java.util.Optional;

/**
 * 탈퇴 사유 저장소 어댑터.
 */
@Component
@RequiredArgsConstructor
public class WithdrawalReasonPersistenceAdapter implements WithdrawalReasonPort {

    private final WithdrawalReasonJpaRepository withdrawalReasonJpaRepository;
    private final WithdrawalReasonMapper withdrawalReasonMapper;

    /**
     * 탈퇴 사유를 저장한다.
     */
    @Override
    public void save(WithdrawalReason withdrawalReason) {
        withdrawalReasonJpaRepository.save(withdrawalReasonMapper.toEntity(withdrawalReason));
    }

    /**
     * 탈퇴 사유 목록을 최신순으로 조회한다.
     */
    @Override
    public Page<WithdrawalReason> findAll(Pageable pageable) {
        return withdrawalReasonMapper.toPageModel(
            withdrawalReasonJpaRepository.findAllByOrderByCreatedAtDesc(pageable)
        );
    }

    /**
     * ID로 탈퇴 사유를 조회한다.
     */
    @Override
    public Optional<WithdrawalReason> findById(Long id) {
        return withdrawalReasonJpaRepository.findById(id)
                .map(withdrawalReasonMapper::toModel);
    }

    /**
     * ID 존재 여부를 확인한다.
     */
    @Override
    public boolean existsById(Long id) {
        return withdrawalReasonJpaRepository.existsById(id);
    }

    /**
     * 탈퇴 사유를 삭제한다.
     */
    @Override
    public void deleteById(Long id) {
        withdrawalReasonJpaRepository.deleteById(id);
    }
}
