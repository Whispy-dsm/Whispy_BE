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
import whispy_server.whispy.global.exception.ErrorNotificationHandler;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;
import whispy_server.whispy.global.oauth.handler.OauthFailureHandler;
import whispy_server.whispy.global.oauth.handler.OauthSuccessHandler;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;
import whispy_server.whispy.global.security.auth.CustomOauthUserDetailsService;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthSuccessHandler oauthSuccessHandler;
    private final OauthFailureHandler oauthFailureHandler;
    private final CustomOauthUserDetailsService customOauthUserService;
    private final DiscordNotificationService discordNotificationService;
    private final ErrorNotificationHandler errorNotificationHandler;

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈을 생성합니다.
     *
     * BCrypt는 단방향 해시 알고리즘으로, 비밀번호를 안전하게 저장하기 위해 사용됩니다.
     * Salt가 자동으로 생성되며, 동일한 비밀번호도 매번 다른 해시 값을 생성합니다.
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .headers(header -> header
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                                .xssProtection(HeadersConfigurer.XXssConfig::disable)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authΕxception) -> response.
                                sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedΕxception) ->  response.
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
                        .requestMatchers("/oauth2/authorization/google", "/login/oauth2/code/google").permitAll()
                        .requestMatchers("/users/login","/users/register","/users/reissue").permitAll()
                        .requestMatchers("/users/password/reset").permitAll()
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/users/oauth/kakao", "/users/oauth/exchange").permitAll()
                        .requestMatchers("/file/profile_image_folder/**",
                                         "/file/music_folder/**",
                                         "/file/music_banner_image_folder/**",
                                         "/file/music_video_folder/**",
                                         "/file/announcement_banner_image_folder/**"
                        ).permitAll()
                        .requestMatchers("/auth/email/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/webhook/google-play").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .with(new FilterConfig(jwtTokenProvider, objectMapper, discordNotificationService, errorNotificationHandler), Customizer.withDefaults());


                return http.build();

    }
}
