package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userFacadeUseCase.getUserByEmail(email);
                return new AuthDetails(user.email(),user.role().name(), AuthDetails.EMPTY_ATTRIBUTES);
    }
}
