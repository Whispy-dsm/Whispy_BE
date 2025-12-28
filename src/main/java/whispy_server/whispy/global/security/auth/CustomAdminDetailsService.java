package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

/**
 * 관리자 로그인 시 사용할 UserDetailsService 구현체.
 */
@Component
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminFacadeUseCase adminFacadeUseCase;

    /**
     * 관리자 ID (PK)로 사용자 정보를 조회해 UserDetails를 반환한다.
     *
     * @param id 관리자 ID (PK)
     * @return UserDetails 구현체
     */
    @Override
    public UserDetails loadUserByUsername(String id){
        Admin admin = adminFacadeUseCase.getAdminById(Long.parseLong(id));
        return new AuthDetails(admin.id(), Role.ADMIN.name(), AuthDetails.EMPTY_ATTRIBUTES);
    }
}
