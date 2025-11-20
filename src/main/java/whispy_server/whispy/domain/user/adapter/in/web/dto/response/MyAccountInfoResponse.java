package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;

@Schema(description = "내 계정 정보 응답")
public record MyAccountInfoResponse(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl,
        @Schema(description = "이메일", example = "user@example.com")
        String email,
        @Schema(description = "성별")
        Gender gender,
        @Schema(description = "OAuth 제공자", example = "KAKAO")
        String provider,
        @Schema(description = "마스킹된 비밀번호", example = "********")
        String password
) {
    public static MyAccountInfoResponse of(User user, String maskedPassword) {
        return new MyAccountInfoResponse(
                user.name(),
                user.profileImageUrl(),
                user.email(),
                user.gender(),
                user.provider(),
                maskedPassword
        );
    }
}
