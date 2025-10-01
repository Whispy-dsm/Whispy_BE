package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

@Component
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminFacadeUseCase adminFacadeUseCase;

    @Override
    public UserDetails loadUserByUsername(String adminId){
        Admin admin = adminFacadeUseCase.getAdminByAdminId(adminId);
        return new AuthDetails(admin.adminId(), Role.ADMIN.name(), AuthDetails.EMPTY_ATTRIBUTES);
    }
}
