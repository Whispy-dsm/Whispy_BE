package whispy_server.whispy.domain.user.port.in;

import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

@UseCase
public interface OauthUserUseCase {

    User findOrCreateOauthUser(OauthUserInfo oauthUserInfo, String provider);
}
