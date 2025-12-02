package whispy_server.whispy.domain.reason.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

/**
 * 탈퇴 사유 저장 요청 DTO.
 */
@Schema(description = "탈퇴 사유 저장 요청")
public record SaveWithdrawalReasonRequest(
        @Schema(description = "탈퇴 사유 타입", example = "NOT_USEFUL")
        @NotNull(message = "탈퇴 사유 타입은 필수입니다.")
        WithdrawalReasonType reasonType,
        
        @Schema(description = "상세 내용 (기타 선택 시 필수)", example = "더 나은 서비스를 찾았습니다.")
        @Size(max = 1000, message = "상세 내용은 1000자를 초과할 수 없습니다.")
        String detailContent
) {
}
