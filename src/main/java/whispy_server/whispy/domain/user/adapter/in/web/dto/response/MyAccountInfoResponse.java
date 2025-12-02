package whispy_server.whispy.domain.user.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;

/**
 * 내 계정 정보 응답 DTO.
 * 사용자의 계정 상세 정보를 제공합니다.
 *
 * @param name 사용자 이름
 * @param profileImageUrl 프로필 이미지 URL
 * @param email 이메일 주소
 * @param gender 성별
 * @param provider OAuth 제공자 (GOOGLE, KAKAO, LOCAL)
 * @param password 마스킹된 비밀번호 (보안상 실제 비밀번호는 노출되지 않음)
 */
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
    /**
     * User 도메인 객체로부터 MyAccountInfoResponse를 생성합니다.
     *
     * @param user 사용자 도메인 객체
     * @param maskedPassword 마스킹된 비밀번호
     * @return MyAccountInfoResponse 인스턴스
     */
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
