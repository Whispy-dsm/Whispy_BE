package whispy_server.whispy.global.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import whispy_server.whispy.domain.auth.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.security.auth.AuthDetails;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String SUCCESS_URL = "/oauth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

        TokenResponse tokenResponse = jwtTokenProvider.generateToken(
                authDetails.getUsername(),
                authDetails.role()
        );

        String redirectUrl = UriComponentsBuilder.fromUriString(SUCCESS_URL)
                .queryParam("accessToken", tokenResponse.accessToken())
                .queryParam("refreshToken", tokenResponse.refreshToken())
                .build().toUriString();

        response.sendRedirect(redirectUrl);


    }




}
