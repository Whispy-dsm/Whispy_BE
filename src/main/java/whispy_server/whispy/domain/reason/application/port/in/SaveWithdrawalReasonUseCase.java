package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SaveWithdrawalReasonUseCase {
    void execute(SaveWithdrawalReasonRequest request);
}
