package whispy_server.whispy.domain.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.vo.Profile;
import whispy_server.whispy.domain.user.application.port.in.UserRegisterUseCase;
import whispy_server.whispy.domain.user.application.port.out.ExistsUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.global.exception.domain.user.UserAlreadyExistException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegisterUseCase {

    private final UserSavePort userSavePort;
    private final ExistsUserPort existsUserPort;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PROVIDER = "일반 로그인";

    @Transactional
    @Override
    public void register(RegisterRequest request){

        if(existsUserPort.existsByEmail(request.email())){
            throw UserAlreadyExistException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Profile profile = new Profile(
                request.name(),
                request.profileImageUrl(),
                request.gender()
        );

        User user = new User(
                UUID.randomUUID(),
                request.email(),
                encodedPassword,
                profile,
                Role.USER,
                true,
                0,
                DEFAULT_PROVIDER
        );

        userSavePort.save(user);
    }
}
