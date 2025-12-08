package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.user.model.User;

/**
 * 내 프로필 응답 DTO.
 * 사용자의 기본 프로필 정보를 제공합니다.
 *
 * @param name 사용자 이름
 * @param profileImageUrl 프로필 이미지 URL
 * @param daysSinceRegistration 가입 후 경과한 일수
 */
@Schema(description = "내 프로필 응답")
public record MyProfileResponse(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl,
        @Schema(description = "가입 후 경과 일수", example = "30")
        Long daysSinceRegistration
) {
    /**
     * User 도메인 객체로부터 MyProfileResponse를 생성합니다.
     *
     * @param user 사용자 도메인 객체
     * @return MyProfileResponse 인스턴스
     */
    public static MyProfileResponse from(User user) {
        return new MyProfileResponse(
                user.name(),
                user.profileImageUrl(),
                user.getDaysSinceRegistration()
        );
    }
}
