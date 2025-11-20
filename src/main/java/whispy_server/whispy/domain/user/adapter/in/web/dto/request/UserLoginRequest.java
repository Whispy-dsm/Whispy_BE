package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(

    @Schema(description = "이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    String email,
    @Schema(description = "비밀번호", example = "password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String password,
    @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    String fcmToken
) {
}
