package whispy_server.whispy.domain.admin.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;

import java.util.UUID;

public interface AdminRepository extends CrudRepository<AdminJpaEntity, UUID> {

    AdminJpaEntity findByAdminId(String adminId);
}
