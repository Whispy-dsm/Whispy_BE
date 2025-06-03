package whispy_server.whispy.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.jwt")
public record JwtProperties(
    String header,
    String prefix,
    String secret,
    Long accessExpiration,
    Long refreshExpiration
    ){}
