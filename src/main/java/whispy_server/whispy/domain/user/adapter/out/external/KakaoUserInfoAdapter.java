package whispy_server.whispy.domain.user.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.domain.oauth.InvalidKakaoOauthResponseException;
import whispy_server.whispy.global.feign.kakao.client.KakaoFeignClient;

import java.util.Map;

/**
 * 카카오 사용자 정보 조회 어댑터.
 * 헥사고날 아키텍처의 아웃바운드 어댑터로서 카카오 API와 통신하여 사용자 정보를 가져옵니다.
 */
@Component
@RequiredArgsConstructor
public class KakaoUserInfoAdapter {

    private final KakaoFeignClient kakaoFeignClient;

    /**
     * 카카오 액세스 토큰으로 사용자 정보를 조회합니다.
     *
     * @param accessToken 카카오 액세스 토큰
     * @return 사용자 정보를 담은 Map (kakao_account, properties 등)
     * @throws InvalidKakaoOauthResponseException 응답이 null이거나 비어있는 경우
     */
    public Map<String, Object> fetchUserInfo(String accessToken) {

            Map<String, Object> userInfo = kakaoFeignClient.getUserInfo("bearer " + accessToken);

            if (userInfo == null || userInfo.isEmpty()) {
                throw InvalidKakaoOauthResponseException.EXCEPTION;
            }
            return userInfo;
    }
}
