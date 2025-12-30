package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonsUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.annotation.AdminAction;

/**
 * 탈퇴 사유 목록 조회 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalReasonsService implements GetWithdrawalReasonsUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 탈퇴 사유 목록을 페이지 단위로 조회한다.
     */
    @AdminAction("탈퇴 사유 목록 조회")
    @Override
    @Transactional(readOnly = true)
    public Page<WithdrawalReasonSummaryResponse> execute(Pageable pageable) {
        return withdrawalReasonQueryPort.findAll(pageable)
                .map(WithdrawalReasonSummaryResponse::from);
    }
}
