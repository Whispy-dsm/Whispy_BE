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
     * @throws InvalidStatisticsDateException 미래 날짜를 조회하려는 경우
     */
    @AdminAction("날짜별 탈퇴 이유 목록 조회")
    @Transactional(readOnly = true)
    @Override
    public Page<WithdrawalReasonsByDateResponse> execute(LocalDate date, Pageable pageable) {
        validateDate(date);

        return withdrawalReasonQueryPort.findAllByDate(date, pageable)
                .map(WithdrawalReasonsByDateResponse::from);
    }

    /**
     * 조회 날짜가 미래인지 검증합니다.
     *
     * @param date 조회할 날짜
     * @throws InvalidStatisticsDateException 미래 날짜인 경우
     */
    private void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw InvalidStatisticsDateException.EXCEPTION;
        }
    }
}
