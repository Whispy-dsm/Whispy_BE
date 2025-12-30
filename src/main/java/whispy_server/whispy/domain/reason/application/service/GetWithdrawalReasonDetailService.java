package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonDetailResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonDetailUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.global.exception.domain.reason.WithdrawalReasonNotFoundException;
import whispy_server.whispy.global.annotation.AdminAction;

/**
 * 탈퇴 사유 상세 조회 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalReasonDetailService implements GetWithdrawalReasonDetailUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 탈퇴 사유 상세를 조회한다.
     *
     * @param id 탈퇴 사유 ID
     * @return 상세 응답
     */
    @Override
    @Transactional(readOnly = true)
    @AdminAction("탈퇴 사유 상세 조회")
    public WithdrawalReasonDetailResponse execute(Long id) {
        WithdrawalReason withdrawalReason = withdrawalReasonQueryPort.findById(id)
                .orElseThrow(() -> WithdrawalReasonNotFoundException.EXCEPTION);
        
        return WithdrawalReasonDetailResponse.from(withdrawalReason);
    }
}
