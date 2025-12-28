package whispy_server.whispy.global.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 인증된 사용자의 식별자, 권한, OAuth2 속성을 보유하는 Spring Security {@link UserDetails} 구현체이다.
 * 세션/토큰 기반 인증 모두에서 동일한 Principal 표현을 재사용한다.
 */
public record AuthDetails(
        Long id,
        String role,
        Map<String,Object> attributes

) implements UserDetails, OAuth2User {

    public static final Map<String, Object> EMPTY_ATTRIBUTES = Collections.emptyMap();
    private static final String ROLE_PREFIX = "ROLE_";

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + role));
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes != null ? attributes : EMPTY_ATTRIBUTES;
    }

    @Override
    public String getName() {
        return id.toString();
    }

}
