package whispy_server.whispy.domain.admin.application.port.out;

import whispy_server.whispy.domain.admin.model.Admin;

import java.util.Optional;

public interface QueryAdminPort {

    Optional<Admin> findByAdminId(String adminId);
}
