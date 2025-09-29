package whispy_server.whispy.domain.user.model;

import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.annotation.Aggregate;

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
        String fcmToken

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
                newFcmToken
        );
    }

}

