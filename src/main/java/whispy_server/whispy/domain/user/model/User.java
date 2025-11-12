package whispy_server.whispy.domain.user.model;

import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    public long getDaysSinceRegistration() {
        return ChronoUnit.DAYS.between(this.createdAt, LocalDateTime.now());
    }

}
