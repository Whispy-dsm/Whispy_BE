package whispy_server.whispy.domain.user.model;

import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.user.model.vo.Profile;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record User(
        Long id,
        String email,
        String password,
        Profile profile,
        Role role,
        int coin,
        String provider,
        String fcmToken

) {

    public User updateFcmToken(String newFcmToken) {
        return new User(
                this.id,
                this.email,
                this.password,
                this.profile,
                this.role,
                this.coin,
                this.provider,
                newFcmToken
        );
    }

}

