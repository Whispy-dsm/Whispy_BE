package whispy_server.whispy.global.oauth.dto;

import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.domain.user.model.vo.Profile;


public record OauthUserInfo(String name, String email, String profileImage) {

    public User toUserInfo(String provider, String defaultPassword) {
        return new User(
                null,
                email,
                defaultPassword, //todo 이거 나중에 바꾸기
                new Profile(name, profileImage, Gender.UNKNOWN),
                Role.USER,
                0,
                provider.toUpperCase()
        );
    }
}
