package whispy_server.whispy.domain.reason.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 관리자 관점 탈퇴 사유 목록 조회 유스케이스.
 */
@UseCase
public interface GetWithdrawalReasonsUseCase {
    Page<WithdrawalReasonSummaryResponse> execute(Pageable pageable);
}
