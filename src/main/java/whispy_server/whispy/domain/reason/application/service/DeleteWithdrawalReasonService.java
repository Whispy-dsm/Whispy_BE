package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.application.port.in.DeleteWithdrawalReasonUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonDeletePort;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.exception.domain.reason.WithdrawalReasonNotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteWithdrawalReasonService implements DeleteWithdrawalReasonUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;
    private final WithdrawalReasonDeletePort withdrawalReasonDeletePort;

    @Override
    @Transactional
    public void execute(Long id) {
        if (!withdrawalReasonQueryPort.existsById(id)) {
            throw WithdrawalReasonNotFoundException.EXCEPTION;
        }
        
        withdrawalReasonDeletePort.deleteById(id);
    }
}
