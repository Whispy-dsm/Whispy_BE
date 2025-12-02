package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

import java.util.Map;

/**
 * 구글 OAuth 사용자 정보를 Whispy 도메인 형태로 변환하는 파서.
 */
public final class GoogleOauthUserInfoParser implements OauthUserInfoParser {

    /**
     * Google OAuth 응답 속성을 {@link OauthUserInfo} 로 변환한다.
     */
    @Override
    public OauthUserInfo parse(Map<String, Object> attributes) {
        return new OauthUserInfo(
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture")
        );
    }
}
