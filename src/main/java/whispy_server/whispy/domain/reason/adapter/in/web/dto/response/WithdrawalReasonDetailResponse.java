package whispy_server.whispy.domain.reason.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import java.time.LocalDateTime;

@Schema(description = "탈퇴 사유 상세 응답")
public record WithdrawalReasonDetailResponse(
        @Schema(description = "탈퇴 사유 ID", example = "1")
        Long id,

        @Schema(description = "탈퇴 사유 타입", example = "NOT_USEFUL")
        WithdrawalReasonType reasonType,

        @Schema(description = "상세 내용", example = "더 나은 서비스를 찾았습니다.")
        String detailContent,

        @Schema(description = "생성 시간", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt
) {
    public static WithdrawalReasonDetailResponse from(WithdrawalReason withdrawalReason) {
        return new WithdrawalReasonDetailResponse(
                withdrawalReason.id(),
                withdrawalReason.reasonType(),
                withdrawalReason.detailContent(),
                withdrawalReason.createdAt()
        );
    }
}
