package whispy_server.whispy.global.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.exception.error.ErrorCode;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * OAuth 인증 실패 시 표준 에러 응답을 반환하는 핸들러.
 */
@Component
@RequiredArgsConstructor
public class OauthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        ErrorCode errorCode = ErrorCode.OAUTH_AUTHENTICATION_FAILED;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, request.getRequestURI(), exception);

        response.setStatus(errorCode.getStatusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }


}
