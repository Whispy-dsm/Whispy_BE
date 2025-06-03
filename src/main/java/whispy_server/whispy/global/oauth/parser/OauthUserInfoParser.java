package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

import java.util.Map;

public interface OauthUserInfoParser {

    OauthUserInfo parse(Map<String, Object> attribute);

}
