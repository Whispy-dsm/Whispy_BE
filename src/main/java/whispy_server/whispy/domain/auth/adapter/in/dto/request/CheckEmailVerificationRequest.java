package whispy_server.whispy.domain.auth.adapter.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 이메일 인증 상태 확인 요청 DTO
 *
 * 사용자의 이메일이 인증되었는지 확인하기 위한 요청 데이터를 담고 있습니다.
 * Redis에 저장된 인증 상태를 조회하는 데 사용됩니다.
 */
@Schema(description = "이메일 인증 상태 확인 요청")
public record CheckEmailVerificationRequest(
        @Schema(description = "인증 상태를 확인할 이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email
) {
    @Override
    public String toString() {
        return "CheckEmailVerificationRequest[email=***]";
    }
}
