package whispy_server.whispy.domain.reason.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * 날짜별 탈퇴 통계 응답 DTO.
 *
 * 날짜별 탈퇴 건수를 그래프로 표시하기 위한 데이터입니다.
 *
 * @param date  날짜
 * @param count 탈퇴 건수
 */
@Schema(description = "날짜별 탈퇴 통계 응답")
public record WithdrawalStatisticsByDateResponse(
                @Schema(description = "날짜", example = "2024-01-15") LocalDate date,
                @Schema(description = "탈퇴 건수", example = "5") Integer count) {
}
