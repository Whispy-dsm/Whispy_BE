package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonsUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWithdrawalReasonsService implements GetWithdrawalReasonsUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    @Override
    public Page<WithdrawalReasonSummaryResponse> execute(Pageable pageable) {
        return withdrawalReasonQueryPort.findAll(pageable)
                .map(WithdrawalReasonSummaryResponse::from);
    }
}
