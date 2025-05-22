package whispy_server.whispy.domain.port.in;

import whispy_server.whispy.domain.admin.domain.Admin;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface AdminFacadeUseCase {

    Admin currentAdmin();

    Admin getAdminByAdminId(String adminId);

}
