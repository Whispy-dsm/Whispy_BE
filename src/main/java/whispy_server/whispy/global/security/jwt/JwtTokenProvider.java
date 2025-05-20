package whispy_server.whispy.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import whispy_server.whispy.domain.auth.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.auth.adapter.out.entity.RefreshToken;
import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.auth.adapter.out.persistence.repository.RefreshTokenRepository;
import whispy_server.whispy.global.security.auth.CustomAdminDetailsService;
import whispy_server.whispy.global.security.auth.CustomUserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomAdminDetailsService customAdminDetailsService;

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public TokenResponse generateToken(String id, String role){
        String accessToken = generateAccessToken(id, role, ACCESS_TOKEN, jwtProperties.accessExpiration());
        String refreshToken = generateRefreshToken(id, role, REFRESH_TOKEN, jwtProperties.refreshExpiration());
        refreshTokenRepository.save(
                new RefreshToken(id, refreshToken, jwtProperties.refreshExpiration())
        );
                return new TokenResponse(accessToken, refreshToken);
    }

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

    private String generateRefreshToken(String id, String role, String type, Long exp){

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

    public TokenResponse reissue(String refreshToken){
        if(!isRefreshToken(refreshToken)){
            throw new IllegalArgumentException("일단 커스텀 나중에");
        }
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("나중에 커스텀 exception으로"));

        String id = token.getId();
        String role = getRole(token.getToken());

        TokenResponse tokenResponse = generateToken(id, role);

        token.update(tokenResponse.refreshToken(), jwtProperties.refreshExpiration());
        refreshTokenRepository.save(token);

        return new TokenResponse(tokenResponse.accessToken(), tokenResponse.refreshToken());

    }

    private String getRole(String token){
        return getJwt(token).getBody().get("role").toString();
    }

    private Boolean isRefreshToken(String token){
        if(token == null || token.isEmpty()){
            return false;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String type = claims.get("type", String.class);
            return "refreshToken".equals(type);
        }catch (Exception e){
            return false;
        }
    }

    private Jws<Claims> getJwt(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("나중에 커스텀 추가", e);
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(jwtProperties.header());

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.prefix())
        && bearerToken.length() > jwtProperties.prefix().length() + 1){
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token){
        Claims body = getJwt(token).getBody();
        UserDetails userDetails = getDetails(body);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    private UserDetails getDetails(Claims body){
        String role = body.get("role").toString();

        if(Role.USER.toString().equals("role") || Role.PREMIUM_USER.toString().equals(role)){
            return customAdminDetailsService.loadUserByUsername(body.getSubject());
        }else{
            return customUserDetailsService.loadUserByUsername(body.getSubject());
        }
    }

}
