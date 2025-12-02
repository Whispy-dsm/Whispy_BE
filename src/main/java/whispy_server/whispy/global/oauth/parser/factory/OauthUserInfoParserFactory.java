package whispy_server.whispy.global.oauth.parser.factory;

import whispy_server.whispy.global.exception.domain.oauth.UnsupportedProviderException;
import whispy_server.whispy.global.oauth.parser.GoogleOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.KakaoOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.OauthUserInfoParser;

/**
 * OAuth provider 이름에 따라 알맞은 파서 구현을 제공하는 팩토리.
 */
public class OauthUserInfoParserFactory {

    private static final GoogleOauthUserInfoParser GOOGLE = new GoogleOauthUserInfoParser();
    private static final KakaoOauthUserInfoParser KAKAO = new KakaoOauthUserInfoParser();

    /**
     * provider 식별자를 기반으로 파서를 반환한다.
     */
    public static OauthUserInfoParser getParser(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> GOOGLE;
            case "kakao" -> KAKAO;
            default -> throw UnsupportedProviderException.EXCEPTION;
        };
    }
}
