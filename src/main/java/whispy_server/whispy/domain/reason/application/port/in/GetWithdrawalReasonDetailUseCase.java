package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 단일 탈퇴 사유 상세를 조회하는 유스케이스.
 */
@UseCase
public interface GetWithdrawalReasonDetailUseCase {
    WithdrawalReasonResponse execute(Long id);
}
