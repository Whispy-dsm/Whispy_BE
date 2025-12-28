package whispy_server.whispy.domain.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.security.auth.AuthDetails;

/**
 * 사용자 파사드.
 * 다른 도메인에서 사용자 정보를 조회할 수 있도록 제공하는 파사드 구현체입니다.
 * 횡단 관심사를 처리하기 위한 Facade 패턴을 적용했습니다.
 */
@Component
@RequiredArgsConstructor
public class UserFacade implements UserFacadeUseCase {

    private final QueryUserPort queryUserPort;

    /**
     * 현재 인증된 사용자를 조회합니다.
     * SecurityContext에서 사용자 ID를 가져와 사용자를 조회합니다.
     *
     * @return 현재 인증된 사용자 도메인 객체
     */
    @Override
    public User currentUser(){
        AuthDetails authDetails = (AuthDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserById(authDetails.id());
    }

    /**
     * 사용자 ID로 사용자를 조회합니다.
     *
     * @param userId 조회할 사용자 ID (PK)
     * @return 사용자 도메인 객체
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public User getUserById(Long userId) {
        return queryUserPort.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
