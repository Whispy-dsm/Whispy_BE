package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.user.model.User;

@Schema(description = "내 프로필 응답")
public record MyProfileResponse(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl,
        @Schema(description = "가입 후 경과 일수", example = "30")
        Long daysSinceRegistration
) {
    public static MyProfileResponse from(User user) {
        return new MyProfileResponse(
                user.name(),
                user.profileImageUrl(),
                user.getDaysSinceRegistration()
        );
    }
}
