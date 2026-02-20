package whispy_server.whispy.global.oauth;

import java.time.Duration;

/**
 * OAuth 딥링크 코드 교환 플로우에서 사용하는 상수 모음.
 */
public final class OauthCodeConstants {

    private OauthCodeConstants() {
    }

    public static final String OAUTH_DEEP_LINK_CALLBACK_URI = "whispy://oauth/success";
    public static final String OAUTH_CODE_KEY_PREFIX = "oauth:code:";
    public static final Duration OAUTH_CODE_TTL = Duration.ofMinutes(3);

    /**
     * 일회용 OAuth 코드를 Redis 키 형태로 변환한다.
     *
     * @param code 일회용 OAuth 코드
     * @return Redis 저장 키
     */
    public static String oauthCodeKey(String code) {
        return OAUTH_CODE_KEY_PREFIX + code;
    }
}
