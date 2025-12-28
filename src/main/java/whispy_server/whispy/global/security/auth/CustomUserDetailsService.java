package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

/**
 * 일반 사용자에 대한 UserDetails를 조회하는 서비스.
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 사용자 ID 기준으로 사용자 정보를 조회하고 UserDetails로 변환한다.
     *
     * @param userId 사용자 ID (PK)
     * @return UserDetails 구현체
     */
    @Override
    public UserDetails loadUserByUsername(String userId){
        User user = userFacadeUseCase.getUserById(Long.parseLong(userId));
        return new AuthDetails(user.id(), user.role().name(), AuthDetails.EMPTY_ATTRIBUTES);
    }
}
