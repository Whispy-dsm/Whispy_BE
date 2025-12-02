package whispy_server.whispy.global.oauth.parser;

import whispy_server.whispy.global.oauth.dto.OauthUserInfo;

import java.util.Map;

/**
 * OAuth provider별 사용자 정보 파싱 전략을 정의한 sealed interface.
 */
public sealed interface OauthUserInfoParser
    permits GoogleOauthUserInfoParser, KakaoOauthUserInfoParser {

    /**
     * Provider 응답 맵을 {@link OauthUserInfo} 로 변환한다.
     */
    OauthUserInfo parse(Map<String, Object> attribute);

}
