package whispy_server.whispy.domain.user.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserRegisterUseCase;
import whispy_server.whispy.domain.user.application.port.out.ExistsUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.global.exception.domain.user.UserAlreadyExistException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegisterUseCase {

    private final UserSavePort userSavePort;
    private final ExistsUserPort existsUserPort;
    private final PasswordEncoder passwordEncoder;
    private final InitializeTopicsUseCase initializeTopicsUseCase;

    private static final String DEFAULT_PROVIDER = "일반 로그인";

    @Transactional
    @Override
    public void register(RegisterRequest request){

        if(existsUserPort.existsByEmail(request.email())){
            throw UserAlreadyExistException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                null,
                request.email(),
                encodedPassword,
                request.name(),
                request.profileImageUrl(),
                request.gender(),
                Role.USER,
                DEFAULT_PROVIDER,
                request.fcmToken(),
                null
        );

        userSavePort.save(user);

        if(request.fcmToken() != null){
            initializeTopicsUseCase.execute(user.email(), request.fcmToken());
        }
    }
}
