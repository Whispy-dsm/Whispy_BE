package whispy_server.whispy.global.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoTokenValidator {

    private final RestTemplate restTemplate;
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public Map<String, Object> validateTokenAndGetUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    USER_INFO_URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> userInfo = response.getBody();
            if (userInfo == null) {
                throw new IllegalArgumentException("dd");
            }
            return userInfo;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new IllegalArgumentException("d");
            }
            throw new IllegalArgumentException("dd");
        }
    }
}