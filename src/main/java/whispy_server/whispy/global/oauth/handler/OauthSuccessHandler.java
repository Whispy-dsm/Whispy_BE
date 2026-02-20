package whispy_server.whispy.global.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import whispy_server.whispy.global.oauth.OauthCodeConstants;
import whispy_server.whispy.global.security.auth.AuthDetails;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import java.io.IOException;
import java.util.UUID;

/**
 * OAuth 인증 성공 시 one-time code를 생성하고 앱 딥링크로 리디렉트하는 핸들러.
 */
@RequiredArgsConstructor
@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisUtil redisUtil;

    /**
     * 사용자 식별 정보를 one-time code와 매핑해 저장한 뒤 앱 딥링크로 code를 전달한다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authentication OAuth 인증 결과
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        String code = UUID.randomUUID().toString();
        String codePayload = authDetails.getUsername() + ":" + authDetails.role();

        redisUtil.set(
                OauthCodeConstants.oauthCodeKey(code),
                codePayload,
                OauthCodeConstants.OAUTH_CODE_TTL
        );

        String redirectUrl = UriComponentsBuilder.fromUriString(OauthCodeConstants.OAUTH_DEEP_LINK_CALLBACK_URI)
                .queryParam("code", code)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUrl);

    }
}
