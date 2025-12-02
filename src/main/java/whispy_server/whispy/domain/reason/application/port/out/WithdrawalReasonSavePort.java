package whispy_server.whispy.domain.reason.application.port.out;

import whispy_server.whispy.domain.reason.model.WithdrawalReason;

/**
 * 탈퇴 사유 저장 Port.
 */
public interface WithdrawalReasonSavePort {
    /**
     * 탈퇴 사유를 저장한다.
     */
    void save(WithdrawalReason withdrawalReason);
}
