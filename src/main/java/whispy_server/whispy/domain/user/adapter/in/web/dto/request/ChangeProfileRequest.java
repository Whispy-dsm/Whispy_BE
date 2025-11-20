package whispy_server.whispy.domain.user.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import whispy_server.whispy.domain.user.model.types.Gender;

@Schema(description = "프로필 변경 요청")
public record ChangeProfileRequest(
        @Schema(description = "이름 (1-30자)", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 1, max = 30)
        @NotBlank
        String name,
        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String profileImageUrl,
        @Schema(description = "성별", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Gender gender
) {
}
