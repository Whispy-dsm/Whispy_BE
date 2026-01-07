package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalStatisticsByDateUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.global.annotation.AdminAction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 날짜별 탈퇴 통계 조회 서비스.
 */
@Service
@RequiredArgsConstructor
public class GetWithdrawalStatisticsByDateService implements GetWithdrawalStatisticsByDateUseCase {

    private final WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 기간 내 날짜별 탈퇴 통계를 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 날짜별 탈퇴 통계 목록
     */
    @AdminAction("날짜별 탈퇴 통계 조회")
    @Transactional(readOnly = true)
    @Override
    public List<WithdrawalStatisticsByDateResponse> execute(LocalDate startDate, LocalDate endDate) {
        List<WithdrawalReason> reasons = withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate);

        // 날짜별로 그룹화하여 카운트
        Map<LocalDate, Long> countByDate = reasons.stream()
                .collect(Collectors.groupingBy(
                        reason -> reason.createdAt().toLocalDate(),
                        Collectors.counting()
                ));

        // LocalDate 오름차순으로 정렬하여 반환
        return countByDate.entrySet().stream()
                .map(entry -> new WithdrawalStatisticsByDateResponse(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> a.date().compareTo(b.date()))
                .toList();
    }
}
