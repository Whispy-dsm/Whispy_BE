package whispy_server.whispy.domain.admin.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.domain.admin.application.port.out.QueryAdminPort;
import whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException;

@Component
@RequiredArgsConstructor
public class AdminFacade implements AdminFacadeUseCase {

    private final QueryAdminPort queryAdminPort;

    @Override
    public Admin currentAdmin(){
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getAdminByAdminId(adminId);
    }

    @Override
    public Admin getAdminByAdminId(String adminId){
        Admin admin = queryAdminPort.findByAdminId(adminId);
        if(admin == null){
            throw AdminNotFoundException.EXCEPTION;
        }
        return admin;

    }
}
