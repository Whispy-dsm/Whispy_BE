package whispy_server.whispy.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.security.jwt.domain.entity.RefreshToken;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.global.exception.domain.security.ExpiredTokenException;
import whispy_server.whispy.global.exception.domain.security.InvalidJwtException;
import whispy_server.whispy.global.exception.domain.security.TokenTooLargeException;
import whispy_server.whispy.global.security.auth.CustomAdminDetailsService;
import whispy_server.whispy.global.security.auth.CustomUserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 생성, 검증, 재발급과 관련된 핵심 유틸리티 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomAdminDetailsService customAdminDetailsService;

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * JWT 토큰의 최대 허용 크기 (바이트 단위)
     * 일반적인 JWT는 200-500바이트, 여유있게 2KB로 제한
     */
    private static final int MAX_TOKEN_SIZE = 2048;

    /**
     * 사용자 식별자/역할을 기반으로 Access·Refresh 토큰을 생성·저장한다.
     */
    public TokenResponse generateToken(String id, String role){
        String accessToken = generateAccessToken(id, role, ACCESS_TOKEN, jwtProperties.accessExpiration());
        String refreshToken = generateRefreshToken(role, REFRESH_TOKEN, jwtProperties.refreshExpiration());
        refreshTokenRepository.save(
                new RefreshToken(id, refreshToken, jwtProperties.refreshExpiration())
        );
                return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * JWT 서명에 사용할 SecretKey를 반환한다.
     */
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }


    private String generateAccessToken(String id, String role, String type, Long exp){

        Date now = new Date();

        return Jwts.builder()
                .setSubject(id)
                .claim("type", type)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ exp * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(String role, String type, Long exp){

        Date now = new Date();

        return Jwts.builder()
                .claim("type", type)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ exp * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰을 검증한 뒤 새 토큰 쌍을 발급하고 저장소를 갱신한다.
     */
    public TokenResponse reissue(String refreshToken){
        if(!isRefreshToken(refreshToken)){
            throw InvalidJwtException.EXCEPTION;
        }
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> InvalidJwtException.EXCEPTION);

        String id = token.getId();
        String role = getRole(token.getToken());

        TokenResponse tokenResponse = generateToken(id, role);

        token.update(tokenResponse.refreshToken(), jwtProperties.refreshExpiration());
        refreshTokenRepository.save(token);

        return new TokenResponse(tokenResponse.accessToken(), tokenResponse.refreshToken());

    }

    private String getRole(String token){
        return getJws(token).getBody().get("role").toString();
    }

    private Boolean isRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Claims claims = getJws(token).getBody();
            String type = claims.get("type", String.class);
            return REFRESH_TOKEN.equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    private Jws<Claims> getJws(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        }catch (Exception e) {
            throw InvalidJwtException.EXCEPTION;
        }
    }

    /**
     * 요청 헤더에서 Bearer 토큰 문자열을 추출한다.
     * 토큰 크기가 최대 허용 크기를 초과하면 예외를 발생시킨다.
     */
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(jwtProperties.header());

        if(StringUtils.hasText(bearerToken)) {
            // 토큰 크기 검증 (조기 차단)
            if(bearerToken.getBytes(StandardCharsets.UTF_8).length > MAX_TOKEN_SIZE) {
                throw TokenTooLargeException.EXCEPTION;
            }

            if(bearerToken.startsWith(jwtProperties.prefix())
                    && bearerToken.length() > jwtProperties.prefix().length() + 1){
                return bearerToken.substring(jwtProperties.prefix().length() +1);
            }
        }
        return null;
    }

    /**
     * 토큰의 subject/role 정보를 바탕으로 Authentication 객체를 생성한다.
     */
    public Authentication getAuthentication(String token){
        Claims body = getJws(token).getBody();
        UserDetails userDetails = getDetails(body);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    private UserDetails getDetails(Claims body){
        String role = body.get("role").toString();

        if(Role.ADMIN.toString().equals(role)){
            return customAdminDetailsService.loadUserByUsername(body.getSubject());
        }else{
            return customUserDetailsService.loadUserByUsername(body.getSubject());
        }
    }
}
