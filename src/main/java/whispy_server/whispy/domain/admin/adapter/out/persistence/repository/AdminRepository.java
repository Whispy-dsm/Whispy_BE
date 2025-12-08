package whispy_server.whispy.domain.admin.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * 관리자 레포지토리 인터페이스
 * <p>
 * Spring Data JPA의 CrudRepository를 확장하여 관리자 엔티티에 대한 데이터베이스 작업을 제공합니다.
 * 헥사고날 아키텍처의 아웃바운드 어댑터 계층에서 실제 데이터베이스 접근을 담당합니다.
 * </p>
 */
public interface AdminRepository extends CrudRepository<AdminJpaEntity, UUID> {

    /**
     * 관리자 로그인 ID로 관리자 엔티티를 조회합니다.
     *
     * @param adminId 조회할 관리자 로그인 ID
     * @return 조회된 관리자 엔티티 (Optional)
     */
    Optional<AdminJpaEntity> findByAdminId(String adminId);
}
