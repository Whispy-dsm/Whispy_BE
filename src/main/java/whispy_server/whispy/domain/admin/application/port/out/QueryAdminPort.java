package whispy_server.whispy.domain.admin.application.port.out;

import whispy_server.whispy.domain.admin.model.Admin;

import java.util.Optional;

/**
 * 관리자 조회 아웃바운드 포트 인터페이스
 *
 * 헥사고날 아키텍처의 아웃바운드 포트로, 관리자 조회 작업을 정의합니다.
 * 애플리케이션 계층이 영속성 계층에 의존하지 않도록 추상화된 인터페이스를 제공합니다.
 */
public interface QueryAdminPort {

    /**
     * 관리자 로그인 ID로 관리자 정보를 조회합니다.
     *
     * @param adminId 조회할 관리자 로그인 ID
     * @return 조회된 관리자 정보 (Optional)
     */
    Optional<Admin> findByAdminId(String adminId);
}
