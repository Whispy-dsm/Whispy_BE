package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 회원 탈퇴 전 입력한 사유를 저장하는 유스케이스.
 */
@UseCase
public interface SaveWithdrawalReasonUseCase {
    void execute(SaveWithdrawalReasonRequest request);
}
