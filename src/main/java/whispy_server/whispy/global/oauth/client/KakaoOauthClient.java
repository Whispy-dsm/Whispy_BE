package whispy_server.whispy.global.oauth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import whispy_server.whispy.global.exception.domain.oauth.InvalidKakaoAccessTokenException;
import whispy_server.whispy.global.exception.domain.oauth.InvalidKakaoOauthResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOauthClient {

    private final RestTemplate restTemplate;

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public Map<String, Object> fetchUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    USER_INFO_URL,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> userInfo = response.getBody();
            if (userInfo == null || userInfo.isEmpty()) {
                throw InvalidKakaoOauthResponseException.EXCEPTION;
            }
            return userInfo;

        } catch (HttpClientErrorException.Unauthorized e) {
            throw InvalidKakaoAccessTokenException.EXCEPTION;
        }
    }
}
