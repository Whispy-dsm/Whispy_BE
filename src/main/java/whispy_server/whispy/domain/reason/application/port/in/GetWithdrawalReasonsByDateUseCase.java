package whispy_server.whispy.domain.reason.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonsByDateResponse;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

/**
 * 날짜 범위별 탈퇴 이유 목록 조회 유스케이스.
 */
@UseCase
public interface GetWithdrawalReasonsByDateUseCase {
    /**
     * 날짜 범위 내의 탈퇴 이유 목록을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param pageable 페이지 정보
     * @return 탈퇴 이유 목록 페이지
     */
    Page<WithdrawalReasonsByDateResponse> execute(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
