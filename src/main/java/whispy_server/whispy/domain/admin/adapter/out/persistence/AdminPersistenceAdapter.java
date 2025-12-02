package whispy_server.whispy.domain.admin.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.adapter.out.persistence.repository.AdminRepository;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.adapter.out.mapper.AdminEntityMapper;
import whispy_server.whispy.domain.admin.application.port.out.AdminPort;

import java.util.Optional;

/**
 * 관리자 영속성 어댑터
 * <p>
 * 헥사고날 아키텍처의 아웃바운드 어댑터로, AdminPort 인터페이스를 구현합니다.
 * 관리자 도메인의 데이터베이스 접근을 담당하며, 도메인 로직과 영속성 기술을 분리합니다.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class AdminPersistenceAdapter implements AdminPort {

    private final AdminEntityMapper adminEntityMapper;
    private final AdminRepository adminRepository;

    /**
     * 관리자 ID로 관리자 정보를 조회합니다.
     *
     * @param adminId 조회할 관리자 로그인 ID
     * @return 조회된 관리자 정보 (Optional)
     */
    @Override
    public Optional<Admin> findByAdminId(String adminId) {
        return adminEntityMapper.toOptionalModel(adminRepository.findByAdminId(adminId));
    }
}
