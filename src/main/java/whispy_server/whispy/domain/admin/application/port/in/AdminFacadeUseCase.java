package whispy_server.whispy.domain.admin.application.port.in;

import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 관리자 파사드 유스케이스 인터페이스
 * <p>
 * 헥사고날 아키텍처의 인바운드 포트로, 관리자 관련 횡단 관심사 기능을 제공합니다.
 * 현재 인증된 관리자 정보 조회 등의 공통 작업을 담당합니다.
 * </p>
 */
@UseCase
public interface AdminFacadeUseCase {

    /**
     * 현재 인증된 관리자 정보를 조회합니다.
     * SecurityContext에서 관리자 ID를 추출하여 관리자 정보를 반환합니다.
     *
     * @return 현재 인증된 관리자 정보
     * @throws whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException 관리자를 찾을 수 없는 경우
     */
    Admin currentAdmin();

    /**
     * 관리자 로그인 ID로 관리자 정보를 조회합니다.
     *
     * @param adminId 조회할 관리자 로그인 ID
     * @return 조회된 관리자 정보
     * @throws whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException 관리자를 찾을 수 없는 경우
     */
    Admin getAdminByAdminId(String adminId);

}
