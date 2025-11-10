package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface DeleteWithdrawalReasonUseCase {
    void execute(Long id);
}
