package whispy_server.whispy.domain.admin.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.adapter.out.persistence.repository.AdminRepository;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.adapter.out.mapper.AdminEntityMapper;
import whispy_server.whispy.domain.admin.application.port.out.AdminPort;

@Component
@RequiredArgsConstructor
public class AdminPersistenceAdapter implements AdminPort {

    private final AdminEntityMapper adminEntityMapper;
    private final AdminRepository adminRepository;

    @Override
    public Admin findByAdminId(String adminId) {
        return adminEntityMapper.toDomain(adminRepository.findByAdminId(adminId));
    }
}
