package whispy_server.whispy.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.domain.user.port.in.OauthUserUseCase;
import whispy_server.whispy.domain.user.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.port.out.UserSavePort;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

@RequiredArgsConstructor
@Transactional
@Service
public class OauthUserService implements OauthUserUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;

    @Override
    public User findOrCreateOauthUser(OauthUserInfo oauthUserInfo, String provider){
        try{
            return queryUserPort.findByEmail(oauthUserInfo.email());
        }catch (Exception e){
            return userSavePort.save(oauthUserInfo.toUserInfo(provider))
        }
    }
}
