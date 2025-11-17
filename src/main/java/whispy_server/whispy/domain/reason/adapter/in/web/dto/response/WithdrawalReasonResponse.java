package whispy_server.whispy.domain.reason.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import java.time.LocalDateTime;

@Schema(description = "탈퇴 사유 응답 (목록용)")
public record WithdrawalReasonResponse(
        @Schema(description = "탈퇴 사유 ID", example = "1")
        Long id,

        @Schema(description = "탈퇴 사유 타입", example = "NOT_USEFUL")
        WithdrawalReasonType reasonType,

        @Schema(description = "생성 시간", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt
) {
    public static WithdrawalReasonResponse from(WithdrawalReason withdrawalReason) {
        return new WithdrawalReasonResponse(
                withdrawalReason.id(),
                withdrawalReason.reasonType(),
                withdrawalReason.createdAt()
        );
    }
}
