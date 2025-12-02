package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 탈퇴 사유 레코드를 삭제하는 유스케이스.
 */
@UseCase
public interface DeleteWithdrawalReasonUseCase {
    void execute(Long id);
}
