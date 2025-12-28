package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * 비밀번호 변경 요청 DTO.
 * 인증된 사용자가 자신의 비밀번호를 변경할 때 사용됩니다.
 *
 * @param email 자신의 이메일 (본인 확인용)
 * @param newPassword 새로운 비밀번호 (8자 이상, 숫자 및 특수문자 포함)
 */
@ToString(exclude = {"newPassword"})
@Schema(description = "비밀번호 변경 요청")
public record ChangePasswordRequest(
        @NotBlank
        @Email
        @Size(max = 255)
        @Schema(name = "email", description = "자기 자신의 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")
        @Schema(name = "new_password", description = "비밀번호는 8자 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 합니다.", example = "@new_password123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 8, max = 60)
        @NotBlank
        String newPassword
) {
}