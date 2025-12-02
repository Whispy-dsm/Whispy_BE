package whispy_server.whispy.domain.user.model;

import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 사용자 도메인 모델.
 * DDD의 Aggregate Root로 사용자 관련 비즈니스 로직을 캡슐화합니다.
 * 불변 객체로 설계되어 모든 변경 작업은 새로운 인스턴스를 반환합니다.
 *
 * @param id 사용자 고유 식별자
 * @param email 사용자 이메일 주소
 * @param password 암호화된 비밀번호
 * @param name 사용자 이름
 * @param profileImageUrl 프로필 이미지 URL
 * @param gender 성별 (MALE, FEMALE, UNKNOWN)
 * @param role 사용자 권한 (USER, ADMIN)
 * @param provider OAuth 제공자 (GOOGLE, KAKAO, LOCAL)
 * @param fcmToken Firebase Cloud Messaging 토큰
 * @param createdAt 계정 생성 일시
 */
@Aggregate
public record User(
        Long id,
        String email,
        String password,
        String name,
        String profileImageUrl,
        Gender gender,
        Role role,
        String provider,
        String fcmToken,
        LocalDateTime createdAt

) {

    /**
     * FCM 토큰을 업데이트합니다.
     *
     * @param newFcmToken 새로운 FCM 토큰
     * @return FCM 토큰이 업데이트된 새로운 User 인스턴스
     */
    public User updateFcmToken(String newFcmToken) {
        return new User(
                this.id,
                this.email,
                this.password,
                this.name,
                this.profileImageUrl,
                this.gender,
                this.role,
                this.provider,
                newFcmToken,
                this.createdAt
        );
    }

    /**
     * 비밀번호를 업데이트합니다.
     *
     * @param newPassword 새로운 암호화된 비밀번호
     * @return 비밀번호가 업데이트된 새로운 User 인스턴스
     */
    public User updatePassword(String newPassword) {
        return new User(
                this.id,
                this.email,
                newPassword,
                this.name,
                this.profileImageUrl,
                this.gender,
                this.role,
                this.provider,
                this.fcmToken,
                this.createdAt
        );
    }

    /**
     * 사용자 프로필 정보를 변경합니다.
     *
     * @param name 변경할 사용자 이름
     * @param profileImageUrl 변경할 프로필 이미지 URL
     * @param gender 변경할 성별
     * @return 프로필 정보가 변경된 새로운 User 인스턴스
     */
    public User changeProfile(String name, String profileImageUrl, Gender gender) {
        return new User(
                this.id,
                this.email,
                this.password,
                name,
                profileImageUrl,
                gender,
                this.role,
                this.provider,
                this.fcmToken,
                this.createdAt
        );
    }

    /**
     * 계정 가입 이후 경과한 일수를 계산합니다.
     *
     * @return 가입일부터 현재까지의 경과 일수
     */
    public long getDaysSinceRegistration() {
        return ChronoUnit.DAYS.between(this.createdAt, LocalDateTime.now());
    }

}
