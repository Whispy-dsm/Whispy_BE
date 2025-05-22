package whispy_server.whispy.domain.admin.port.out;

import whispy_server.whispy.domain.admin.domain.Admin;

public interface QueryAdminPort {

    Admin findByAdminId(String adminId);
}
