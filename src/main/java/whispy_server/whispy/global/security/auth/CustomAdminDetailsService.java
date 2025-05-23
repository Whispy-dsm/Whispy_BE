package whispy_server.whispy.global.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.domain.Admin;
import whispy_server.whispy.domain.admin.port.in.AdminFacadeUseCase;

@Component
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminFacadeUseCase adminFacadeUseCase;

    @Override
    public UserDetails loadUserByUsername(String adminId){
        Admin admin = adminFacadeUseCase.getAdminByAdminId(adminId);
        return new AuthDetails(admin.adminId(),admin.role().name());
    }
}
