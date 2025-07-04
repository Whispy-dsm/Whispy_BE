package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

import java.util.Map;

public sealed interface OauthUserInfoParser
    permits GoogleOauthUserInfoParser, KakaoOauthUserInfoParser {

    OauthUserInfo parse(Map<String, Object> attribute);

}