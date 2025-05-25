package whispy_server.whispy.global.oauth.parser.factory;

import whispy_server.whispy.global.exception.domain.oauth.UnsupportedProviderException;
import whispy_server.whispy.global.oauth.parser.GoogleOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.KakaoOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.OauthUserInfoParser;

public class OauthUserInfoParserFactory {
    public static OauthUserInfoParser getParser(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> new GoogleOauthUserInfoParser();
            case "kakao" -> new KakaoOauthUserInfoParser();
            default -> throw UnsupportedProviderException.EXCEPTION;
        };
    }
}
