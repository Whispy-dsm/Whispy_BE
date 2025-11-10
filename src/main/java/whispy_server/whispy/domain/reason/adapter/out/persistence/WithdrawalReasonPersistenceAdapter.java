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

@Component
@RequiredArgsConstructor
public class WithdrawalReasonPersistenceAdapter implements WithdrawalReasonPort {

    private final WithdrawalReasonJpaRepository withdrawalReasonJpaRepository;
    private final WithdrawalReasonMapper withdrawalReasonMapper;

    @Override
    public void save(WithdrawalReason withdrawalReason) {
        withdrawalReasonJpaRepository.save(withdrawalReasonMapper.toEntity(withdrawalReason));
    }

    @Override
    public Page<WithdrawalReason> findAll(Pageable pageable) {
        return withdrawalReasonMapper.toPageModel(
            withdrawalReasonJpaRepository.findAllByOrderByCreatedAtDesc(pageable)
        );
    }

    @Override
    public Optional<WithdrawalReason> findById(Long id) {
        return withdrawalReasonJpaRepository.findById(id)
                .map(withdrawalReasonMapper::toModel);
    }

    @Override
    public boolean existsById(Long id) {
        return withdrawalReasonJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        withdrawalReasonJpaRepository.deleteById(id);
    }
}
