package whispy_server.whispy.global.feign.config;

import feign.RequestInterceptor;
import whispy_server.whispy.global.feign.google.GooglePlayAccessTokenProvider;

public class GooglePlayFeignConfig {

    public RequestInterceptor requestInterceptor(GooglePlayAccessTokenProvider googlePlayAccessTokenProvider) {
        return requestTemplate -> {
            String accessToken = googlePlayAccessTokenProvider.getAccessToken();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }
}
