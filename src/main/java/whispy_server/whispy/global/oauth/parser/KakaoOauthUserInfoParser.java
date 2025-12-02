package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;
import java.util.Map;

/**
 * 카카오 OAuth 사용자 정보를 Whispy 도메인에 맞게 파싱하는 구현체.
 */
public final class KakaoOauthUserInfoParser implements OauthUserInfoParser {

    @SuppressWarnings("unchecked")
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
