package whispy_server.whispy.global.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String header;
    private String prefix;
    private String secret;
    private Long accessExpiration;
    private Long refreshExpiration;
}
