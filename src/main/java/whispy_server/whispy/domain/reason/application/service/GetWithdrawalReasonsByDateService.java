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
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;

/**
 * 날짜 범위별 탈퇴 이유 목록 조회 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalReasonsByDateService implements GetWithdrawalReasonsByDateUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 날짜 범위 내의 탈퇴 이유 목록을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param pageable 페이지 정보
     * @return 탈퇴 이유 목록 페이지
     * @throws InvalidStatisticsDateException 날짜 범위가 유효하지 않은 경우
     */
    @AdminAction("날짜 범위별 탈퇴 이유 목록 조회")
    @Transactional(readOnly = true)
    @Override
    public Page<WithdrawalReasonsByDateResponse> execute(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        validateDateRange(startDate, endDate);

        return withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate, pageable)
                .map(WithdrawalReasonsByDateResponse::from);
    }

    /**
     * 날짜 범위가 유효한지 검증합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @throws InvalidStatisticsDateException 날짜 범위가 유효하지 않은 경우
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
        if (endDate.isAfter(LocalDate.now())) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
    }
}
