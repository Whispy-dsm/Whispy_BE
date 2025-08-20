package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.domain.user.model.vo.Profile;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

@RequiredArgsConstructor
@Service
public class OauthUserService implements OauthUserUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;

    @Value("${spring.oauth.default-password}")
    private String defaultPassword;

    @Override
    @Transactional
    public User findOrCreateOauthUser(OauthUserInfo oauthUserInfo, String provider) {
        return queryUserPort.findByEmail(oauthUserInfo.email())
                .orElseGet(() -> {
                    User newUser = new User(
                            null,
                            oauthUserInfo.email(),
                            defaultPassword,
                            new Profile(oauthUserInfo.name(), oauthUserInfo.profileImage(), Gender.UNKNOWN),
                            Role.USER,
                            0,
                            provider.toUpperCase(),
                            null
                    );
                    userSavePort.save(newUser);
                    return newUser;
                });
    }
}
