package whispy_server.whispy.domain.reason.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetWithdrawalReasonsUseCase {
    Page<WithdrawalReasonSummaryResponse> execute(Pageable pageable);
}
