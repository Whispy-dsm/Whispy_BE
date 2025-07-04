package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import java.util.Map;

public final class KakaoOauthUserInfoParser implements OauthUserInfoParser {

    @Override
    public OauthUserInfo parse(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return new OauthUserInfo(
                (String) profile.get("nickname"),
                (String) account.get("email"),
                (String) profile.get("profile_image_url")
        );
    }
}