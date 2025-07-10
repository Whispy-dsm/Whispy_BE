package whispy_server.whispy.domain.user.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.domain.oauth.InvalidKakaoOauthResponseException;
import whispy_server.whispy.global.feign.kakao.client.KakaoFeignClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoUserInfoAdapter {

    private final KakaoFeignClient kakaoFeignClient;

    public Map<String, Object> fetchUserInfo(String accessToken) {

            Map<String, Object> userInfo = kakaoFeignClient.getUserInfo("bearer " + accessToken);

            if (userInfo == null || userInfo.isEmpty()) {
                throw InvalidKakaoOauthResponseException.EXCEPTION;
            }
            return userInfo;
    }
}
