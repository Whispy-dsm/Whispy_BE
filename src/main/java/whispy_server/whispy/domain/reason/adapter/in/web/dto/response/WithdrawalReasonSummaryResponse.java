package whispy_server.whispy.domain.reason.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import java.time.LocalDateTime;

@Schema(description = "탈퇴 사유 요약 응답 (전체 조회용)")
public record WithdrawalReasonSummaryResponse(
        @Schema(description = "탈퇴 사유 ID", example = "1")
        Long id,

        @Schema(description = "탈퇴 사유 타입", example = "NOT_USEFUL")
        WithdrawalReasonType reasonType,

        @Schema(description = "생성 시간", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt
) {
    public static WithdrawalReasonSummaryResponse from(WithdrawalReason withdrawalReason) {
        return new WithdrawalReasonSummaryResponse(
                withdrawalReason.id(),
                withdrawalReason.reasonType(),
                withdrawalReason.createdAt()
        );
    }
}
