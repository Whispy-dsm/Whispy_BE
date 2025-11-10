package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonDetailUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.global.exception.domain.reason.WithdrawalReasonNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWithdrawalReasonDetailService implements GetWithdrawalReasonDetailUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    @Override
    public WithdrawalReasonResponse execute(Long id) {
        WithdrawalReason withdrawalReason = withdrawalReasonQueryPort.findById(id)
                .orElseThrow(() -> WithdrawalReasonNotFoundException.EXCEPTION);
        
        return WithdrawalReasonResponse.from(withdrawalReason);
    }
}
