package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.ToString;
import whispy_server.whispy.domain.user.model.types.Gender;

/**
 * 회원가입 요청 DTO.
 * 로컬 계정으로 회원가입할 때 사용됩니다.
 *
 * @param email 이메일 주소
 * @param password 비밀번호 (8자 이상, 숫자 및 특수문자 포함)
 * @param name 사용자 이름 (1-30자)
 * @param profileImageUrl 프로필 이미지 URL (선택)
 * @param gender 성별 (선택)
 * @param fcmToken Firebase Cloud Messaging 토큰 (선택)
 * @param isEventAgreed 이벤트 알림 수신 동의 여부
 */
@ToString(exclude = {"password", "fcmToken"})
@Schema(description = "회원가입 요청")
public record RegisterRequest(
        @Email
        @NotBlank
        @Size(max = 255)
        @Schema(name = "email", description = "이메일 형식대로 작성해야 한다.", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")
        @Schema(name = "password", description = "비밀번호는 8자 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 합니다.", example = "@pasd1sword123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 8, max = 60)
        @NotBlank
        String password,

        @Schema(name = "name", description = "이름은 30자 이내로 작성되어야 한다", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 1, max = 30)
        @NotBlank
        String name,

        @Schema(name = "profile_image_url", description = "이미지 URL", example = "https://example.com/profile.jpg")
        @Size(max = 500)
        String profileImageUrl,

        @Schema(name = "gender", description = "자신의 성 ( ENUM 형태 ) ")
        Gender gender,

        @Schema(name = "fcm_token", description = "fcm 토큰", example = "fGcm_T0k3n_ExAmPlE...")
        @Size(max = 255)
        String fcmToken,

        @Schema(name = "is_event_agreed", description = "이벤트 알림 수신 동의 여부", example = "true")
        boolean isEventAgreed
) {
}
