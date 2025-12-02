package whispy_server.whispy.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * JWT 서명 및 만료시간 정보를 담는 구성 프로퍼티.
 */
@ConfigurationProperties(prefix = "spring.jwt")
public record JwtProperties(
    String header,
    String prefix,
    String secret,
    Long accessExpiration,
    Long refreshExpiration
    ){}
