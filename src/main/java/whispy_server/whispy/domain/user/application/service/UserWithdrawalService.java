package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.auth.adapter.out.persistence.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserWithdrawalUseCase;
import whispy_server.whispy.domain.user.application.port.out.UserDeletePort;
import whispy_server.whispy.domain.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWithdrawalService implements UserWithdrawalUseCase {

    private final UserDeletePort userDeletePort;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacadeUseCase userFacadeUseCase;

    @Override
    public void withdrawal(){
        User currentUser = userFacadeUseCase.currentUser();
        refreshTokenRepository.deleteById(currentUser.email());
        userDeletePort.deleteByUUID(currentUser.id());
    }
}
