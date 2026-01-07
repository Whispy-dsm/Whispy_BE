package whispy_server.whispy.domain.reason.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonsByDateResponse;
import whispy_server.whispy.global.annotation.UseCase;

import java.time.LocalDate;

/**
 * 날짜별 탈퇴 이유 목록 조회 유스케이스.
 */
@UseCase
public interface GetWithdrawalReasonsByDateUseCase {
    /**
     * 특정 날짜의 탈퇴 이유 목록을 조회합니다.
     *
     * @param date 조회할 날짜
     * @param pageable 페이지 정보
     * @return 탈퇴 이유 목록 페이지
     */
    Page<WithdrawalReasonsByDateResponse> execute(LocalDate date, Pageable pageable);
}
