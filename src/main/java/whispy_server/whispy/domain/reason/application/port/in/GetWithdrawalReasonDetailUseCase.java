package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetWithdrawalReasonDetailUseCase {
    WithdrawalReasonResponse execute(Long id);
}
