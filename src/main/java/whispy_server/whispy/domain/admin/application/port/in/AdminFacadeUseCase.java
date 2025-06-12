package whispy_server.whispy.domain.admin.application.port.in;

import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface AdminFacadeUseCase {

    Admin currentAdmin();

    Admin getAdminByAdminId(String adminId);

}
