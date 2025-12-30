package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.application.port.in.DeleteWithdrawalReasonUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonDeletePort;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.annotation.AdminAction;
import whispy_server.whispy.global.exception.domain.reason.WithdrawalReasonNotFoundException;

/**
 * 탈퇴 사유 삭제 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class DeleteWithdrawalReasonService implements DeleteWithdrawalReasonUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;
    private final WithdrawalReasonDeletePort withdrawalReasonDeletePort;

    /**
     * 탈퇴 사유를 삭제한다.
     *
     * @param id 삭제할 ID
     */
    @AdminAction("탈퇴 사유 삭제")
    @Override
    @Transactional
    public void execute(Long id) {
        if (!withdrawalReasonQueryPort.existsById(id)) {
            throw WithdrawalReasonNotFoundException.EXCEPTION;
        }
        
        withdrawalReasonDeletePort.deleteById(id);
    }
}
