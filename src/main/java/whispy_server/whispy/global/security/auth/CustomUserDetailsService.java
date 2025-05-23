package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.domain.User;
import whispy_server.whispy.domain.user.port.in.UserFacadeUseCase;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userFacadeUseCase.getUserByEmail(email);
                return new AuthDetails(user.email(),user.role().name());
    }
}
