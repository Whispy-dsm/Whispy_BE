package whispy_server.whispy.domain.admin.application.port.out;

import whispy_server.whispy.domain.admin.model.Admin;

public interface QueryAdminPort {

    Admin findByAdminId(String adminId);
}
