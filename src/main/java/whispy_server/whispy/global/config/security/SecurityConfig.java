package whispy_server.whispy.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import whispy_server.whispy.global.config.filter.FilterConfig;
import whispy_server.whispy.global.oauth.handler.OauthFailureHandler;
import whispy_server.whispy.global.oauth.handler.OauthSuccessHandler;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.oauth.CustomOauthUserService;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthSuccessHandler oauthSuccessHandler;
    private final OauthFailureHandler oauthFailureHandler;
    private final CustomOauthUserService customOauthUserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .headers(header -> header
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                                .xssProtection(HeadersConfigurer.XXssConfig::disable)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.
                                sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->  response.
                                sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied"))
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauthSuccessHandler)
                        .failureHandler(oauthFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauthUserService)
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/users/login","/users/register","/users/reissue").permitAll()
                        .requestMatchers("/oauth/success/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )

                .with(new FilterConfig(jwtTokenProvider, objectMapper), Customizer.withDefaults());


                return http.build();

    }
}