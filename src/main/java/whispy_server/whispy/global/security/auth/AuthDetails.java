package whispy_server.whispy.global.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record AuthDetails(
        String id,
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
        return id;
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
        return id;
    }

}
