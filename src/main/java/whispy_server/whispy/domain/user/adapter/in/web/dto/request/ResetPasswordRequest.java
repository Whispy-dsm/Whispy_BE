package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 비밀번호 재설정 요청 DTO.
 * 이메일 인증을 통해 비밀번호를 잊어버린 경우 재설정할 때 사용됩니다.
 *
 * @param email 이메일 주소
 * @param newPassword 새로운 비밀번호 (8자 이상, 숫자 및 특수문자 포함)
 */
@Schema(description = "비밀번호 재설정 요청")
public record ResetPasswordRequest(
        @Schema(description = "이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        @NotBlank
        @Size(max = 255)
        String email,

        @Schema(description = "새 비밀번호 (8자 이상, 숫자 및 특수문자 포함)", example = "newPassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")
        @Size(min = 8, max = 60)
        @NotBlank
        String newPassword
) {
    @Override
    public String toString() {
        return "ResetPasswordRequest[email=" + email + ", newPassword=***]";
    }
}
