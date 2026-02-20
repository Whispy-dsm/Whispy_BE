package whispy_server.whispy.global.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.oauth.OauthCodeConstants;

import java.io.IOException;

/**
 * OAuth 인증 실패 시 오류 정보를 앱 딥링크로 전달하는 핸들러.
 */
@Component
@RequiredArgsConstructor
public class OauthFailureHandler implements AuthenticationFailureHandler {

    /**
     * OAuth 실패 정보를 error, error_description 쿼리 파라미터로 구성해 앱으로 리디렉트한다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param exception 인증 실패 예외
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        ErrorCode errorCode = ErrorCode.OAUTH_AUTHENTICATION_FAILED;
        String errorDescription = exception.getMessage() == null ? "OAuth2 인증에 실패했습니다" : exception.getMessage();

        String redirectUrl = UriComponentsBuilder.fromUriString(OauthCodeConstants.OAUTH_DEEP_LINK_SUCCESS_URL)
                .queryParam("error", errorCode.name().toLowerCase())
                .queryParam("error_description", errorDescription)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }


}
