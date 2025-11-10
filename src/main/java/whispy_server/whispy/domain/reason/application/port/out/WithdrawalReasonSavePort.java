package whispy_server.whispy.domain.reason.application.port.out;

import whispy_server.whispy.domain.reason.model.WithdrawalReason;

public interface WithdrawalReasonSavePort {
    void save(WithdrawalReason withdrawalReason);
}
