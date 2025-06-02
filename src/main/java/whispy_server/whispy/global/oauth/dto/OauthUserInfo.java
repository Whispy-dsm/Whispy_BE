package whispy_server.whispy.global.oauth.dto;

import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.domain.user.domain.types.Gender;
import whispy_server.whispy.domain.user.domain.vo.Profile;

import java.util.UUID;

public record OauthUserInfo(String name, String email, String profileImage) {

    public User toUserInfo(String provider) {
        return new User(
                UUID.randomUUID(),
                email,
                null,
                new Profile(name,profileImage,Gender.UNKNOWN),
                Role.USER,
                true,
                0,
                provider.toUpperCase()
        );
    }
}
