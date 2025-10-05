package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordRequest(
        @NotBlank
        @Schema(name = "current_password", description = "현재 비밀번호")
        String currentPassword,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")
        @Schema(name = "new_password", description = "비밀번호는 8자 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 합니다.", example = "@new_password123!")
        @Size(min = 8, max = 60)
        @NotBlank
        String newPassword
) {
}
