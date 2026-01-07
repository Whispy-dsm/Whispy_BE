package whispy_server.whispy.domain.reason.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import java.time.LocalDateTime;

/**
 * 날짜별 탈퇴 이유 응답 DTO.
 *
 * 특정 날짜의 탈퇴 이유를 조회할 때 사용됩니다.
 *
 * @param id 탈퇴 이유 ID
 * @param reasonType 사유 타입
 * @param detailContent 상세 내용
 * @param createdAt 생성 일시
 */
@Schema(description = "날짜별 탈퇴 이유 응답")
public record WithdrawalReasonsByDateResponse(
        @Schema(description = "탈퇴 이유 ID", example = "1")
        Long id,
        @Schema(description = "사유 타입")
        WithdrawalReasonType reasonType,
        @Schema(description = "상세 내용", example = "음악이 너무 적어요")
        String detailContent,
        @Schema(description = "생성 일시", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt
) {
    /**
     * WithdrawalReason 도메인 모델을 응답 DTO로 변환합니다.
     *
     * @param reason 탈퇴 이유 도메인 모델
     * @return 날짜별 탈퇴 이유 응답 DTO
     */
    public static WithdrawalReasonsByDateResponse from(WithdrawalReason reason) {
        return new WithdrawalReasonsByDateResponse(
                reason.id(),
                reason.reasonType(),
                reason.detailContent(),
                reason.createdAt()
        );
    }
}
