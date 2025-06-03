package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

import java.util.Map;

public class GoogleOauthUserInfoParser implements OauthUserInfoParser {

    @Override
    public OauthUserInfo parse(Map<String, Object> attributes) {
        return new OauthUserInfo(
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture")
        );
    }
}
