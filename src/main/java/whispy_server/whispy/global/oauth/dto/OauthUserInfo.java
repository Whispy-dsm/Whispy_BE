package whispy_server.whispy.global.oauth.dto;

import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.domain.user.model.vo.Profile;

import java.util.UUID;

public record OauthUserInfo(String name, String email, String profileImage) {
    private static final String DEFAULT_PASSWORD = "OAUTH_USER";

    public User toUserInfo(String provider) {
        return new User(
                UUID.randomUUID(),
                email,
                DEFAULT_PASSWORD, //todo 고쳐야 함 이거 password
                new Profile(name, profileImage,Gender.UNKNOWN),
                Role.USER,
                true,
                0,
                provider.toUpperCase()
        );
    }
}
