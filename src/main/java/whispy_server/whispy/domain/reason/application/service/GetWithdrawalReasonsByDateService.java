package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonsByDateResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonsByDateUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.global.annotation.AdminAction;

import java.time.LocalDate;

/**
 * 날짜별 탈퇴 이유 목록 조회 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalReasonsByDateService implements GetWithdrawalReasonsByDateUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 특정 날짜의 탈퇴 이유 목록을 조회합니다.
     *
     * @param date 조회할 날짜
     * @param pageable 페이지 정보
     * @return 탈퇴 이유 목록 페이지
     */
    @AdminAction("날짜별 탈퇴 이유 목록 조회")
    @Transactional(readOnly = true)
    @Override
    public Page<WithdrawalReasonsByDateResponse> execute(LocalDate date, Pageable pageable) {
        return withdrawalReasonQueryPort.findAllByDate(date, pageable)
                .map(WithdrawalReasonsByDateResponse::from);
    }
}
