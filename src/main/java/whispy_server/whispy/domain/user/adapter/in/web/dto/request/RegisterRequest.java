package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.user.model.types.Gender;

public record RegisterRequest(
        @Email
        @Schema(name = "email", description = "이메일 형식대로 작성해야 한다.", example = "user@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")
        @Schema(name = "password", description = "비밀번호는 8자 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 합니다.", example = "@pasd1sword123!")
        @Size(min = 8, max = 60)
        String password,

        @Schema(name = "name", description = "이름은 30자 이내로 작성되어야 한다", example = "홍길동")
        @Size(min = 1, max = 30)
        String name,

        @Schema(name = "profile_image_url", description = "이미지 URL")
        String profileImageUrl,

        @Schema(name = "gender", description = "자신의 성 ( ENUM 형태 ) ")
        Gender gender,

        @NotBlank
        String fcmToken
) {
}
