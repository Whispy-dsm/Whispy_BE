package whispy_server.whispy.global.feign.kakao.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * 카카오 사용자 정보 API 를 호출하기 위한 Feign 클라이언트.
 */
@FeignClient(name = "kakao-client", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {

    @GetMapping("/v2/user/me")
    Map<String, Object> getUserInfo(@RequestHeader("Authorization") String bearerToken);
}
