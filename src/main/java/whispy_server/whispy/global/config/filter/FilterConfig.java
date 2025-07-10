package whispy_server.whispy.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import whispy_server.whispy.global.exception.ErrorNotificationHandler;
import whispy_server.whispy.global.exception.GlobalExceptionFilter;
import whispy_server.whispy.global.feign.discord.DiscordNotificationService;
import whispy_server.whispy.global.security.jwt.JwtTokenFilter;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Configuration
public class FilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final DiscordNotificationService discordNotificationService;
    private final ErrorNotificationHandler errorNotificationHandler;

    @Override
    public void configure(HttpSecurity http){
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        GlobalExceptionFilter globalExceptionFilter = new GlobalExceptionFilter(objectMapper, discordNotificationService, errorNotificationHandler);

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(globalExceptionFilter, JwtTokenFilter.class);
    }
}
