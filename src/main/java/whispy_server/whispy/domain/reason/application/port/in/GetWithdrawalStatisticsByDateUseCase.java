package whispy_server.whispy.domain.reason.application.port.in;

import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;
import java.util.List;

/**
 * 날짜별 탈퇴 통계 조회 유스케이스.
 */
@UseCase
public interface GetWithdrawalStatisticsByDateUseCase {
    /**
     * 기간 내 날짜별 탈퇴 통계를 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 날짜별 탈퇴 통계 목록
     */
    List<WithdrawalStatisticsByDateResponse> execute(LocalDate startDate, LocalDate endDate);
}
