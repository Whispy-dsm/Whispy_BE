package whispy_server.whispy.global.feign.config;

import feign.RequestInterceptor;
import whispy_server.whispy.global.feign.google.GooglePlayAccessTokenProvider;

/**
 * 구글 플레이 Feign 클라이언트에 Bearer 토큰을 주입하는 설정.
 */
public class GooglePlayFeignConfig {

    /**
     * 요청마다 액세스 토큰을 헤더에 추가하는 인터셉터를 생성한다.
     */
    public RequestInterceptor requestInterceptor(GooglePlayAccessTokenProvider googlePlayAccessTokenProvider) {
        return requestTemplate -> {
            String accessToken = googlePlayAccessTokenProvider.getAccessToken();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }
}
