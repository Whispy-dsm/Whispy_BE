package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import whispy_server.whispy.global.annotation.UserAction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

/**
 * OAuth 사용자 처리 서비스.
 * OAuth 인증 후 사용자를 조회하거나 신규 생성합니다.
 */
@RequiredArgsConstructor
@Service
public class OauthUserService implements OauthUserUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;

    /**
     * OAuth 사용자 정보로 기존 사용자를 조회하거나 신규 생성합니다.
     * 이메일이 존재하면 해당 사용자를 반환하고, 없으면 새로 생성합니다.
     *
     * @param oauthUserInfo OAuth에서 받은 사용자 정보
     * @param provider OAuth 제공자 (GOOGLE, KAKAO)
     * @return 조회되거나 생성된 사용자 도메인 객체
     */
    @Override
    @Transactional
    @UserAction("OAuth 사용자 처리")
    public User findOrCreateOauthUser(OauthUserInfo oauthUserInfo, String provider) {
        return queryUserPort.findByEmail(oauthUserInfo.email())
                .orElseGet(() -> {
                    User newUser = new User(
                            null,
                            oauthUserInfo.email(),
                            null,
                            oauthUserInfo.name(),
                            oauthUserInfo.profileImage(),
                            Gender.UNKNOWN,
                            Role.USER,
                            provider.toUpperCase(),
                            null,
                            null
                    );
                    userSavePort.save(newUser);
                    return newUser;
                });
    }
}
