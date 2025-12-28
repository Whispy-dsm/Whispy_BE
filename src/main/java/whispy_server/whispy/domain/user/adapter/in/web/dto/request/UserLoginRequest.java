package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

/**
 * 사용자 로그인 요청 DTO.
 * 이메일과 비밀번호를 사용한 로컬 로그인에 사용됩니다.
 *
 * @param email 이메일 주소
 * @param password 비밀번호
 * @param fcmToken Firebase Cloud Messaging 토큰
 */
@ToString(exclude = {"password", "fcmToken"})
@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(

    @Schema(description = "이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    @Size(max = 255)
    String email,
    @Schema(description = "비밀번호", example = "password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 8, max = 70)
    String password,
    @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 255)
    String fcmToken
) {
}
