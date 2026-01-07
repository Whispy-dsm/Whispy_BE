package whispy_server.whispy.domain.reason.adapter.out.dto;

import java.time.LocalDate;

/**
 * 날짜별 탈퇴 통계 집계 DTO.
 *
 * QueryDSL 프로젝션을 통해 데이터베이스에서 직접 집계된 결과를 받습니다.
 *
 * @param date  탈퇴 날짜
 * @param count 탈퇴 건수
 */
public record WithdrawalStatisticsDto(
                LocalDate date,
                Integer count) {
}
