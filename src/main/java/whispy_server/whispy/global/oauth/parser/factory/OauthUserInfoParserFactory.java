package whispy_server.whispy.global.oauth.parser.factory;

import whispy_server.whispy.global.exception.domain.oauth.UnsupportedProviderException;
import whispy_server.whispy.global.oauth.parser.GoogleOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.KakaoOauthUserInfoParser;
import whispy_server.whispy.global.oauth.parser.OauthUserInfoParser;

public class OauthUserInfoParserFactory {

    private static final GoogleOauthUserInfoParser GOOGLE = new GoogleOauthUserInfoParser();
    private static final KakaoOauthUserInfoParser KAKAO = new KakaoOauthUserInfoParser();

    public static OauthUserInfoParser getParser(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> GOOGLE;
            case "kakao" -> KAKAO;
            default -> throw UnsupportedProviderException.EXCEPTION;
        };
    }
}
