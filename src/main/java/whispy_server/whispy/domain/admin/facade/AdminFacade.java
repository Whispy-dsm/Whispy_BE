package whispy_server.whispy.domain.admin.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.admin.application.port.in.AdminFacadeUseCase;
import whispy_server.whispy.domain.admin.application.port.out.QueryAdminPort;
import whispy_server.whispy.global.exception.domain.admin.AdminNotFoundException;

/**
 * 관리자 파사드.
 *
 * 관리자 조회를 위한 횡단 관심사를 처리하는 파사드 패턴 구현체입니다.
 * Spring Security Context에서 현재 인증된 관리자 정보를 가져오는 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class AdminFacade implements AdminFacadeUseCase {

    private final QueryAdminPort queryAdminPort;

    /**
     * 현재 인증된 관리자를 조회합니다.
     *
     * Security Context에서 관리자 ID를 가져와 관리자 정보를 반환합니다.
     *
     * @return 현재 인증된 관리자 도메인 모델
     * @throws AdminNotFoundException 관리자를 찾을 수 없는 경우
     */
    @Override
    public Admin currentAdmin(){
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
        return getAdminByAdminId(adminId);
    }

    /**
     * 관리자 ID로 관리자를 조회합니다.
     *
     * @param adminId 조회할 관리자 ID
     * @return 관리자 도메인 모델
     * @throws AdminNotFoundException 관리자를 찾을 수 없는 경우
     */
    @Override
    public Admin getAdminByAdminId(String adminId){
        return queryAdminPort.findByAdminId(adminId)
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION );
    }
}
