package whispy_server.whispy.domain.user.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.file.DefaultImageProperties;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserRegisterUseCase;
import whispy_server.whispy.domain.user.application.port.out.ExistsUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.global.exception.domain.user.UserAlreadyExistException;

import java.time.LocalDateTime;

/**
 * 사용자 회원가입 서비스.
 * 로컬 계정으로 새로운 사용자를 등록합니다.
 */
@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegisterUseCase {

    private final UserSavePort userSavePort;
    private final ExistsUserPort existsUserPort;
    private final PasswordEncoder passwordEncoder;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final DefaultImageProperties defaultImageProperties;

    private static final String DEFAULT_PROVIDER = "일반 로그인";

    /**
     * 새로운 사용자를 회원가입 처리합니다.
     * 이메일 중복 확인, 비밀번호 암호화, FCM 토픽 초기화를 수행합니다.
     *
     * @param request 회원가입 요청 (이메일, 비밀번호, 이름 등)
     */
    @Transactional
    @Override
    public void register(RegisterRequest request){

        if(existsUserPort.existsByEmail(request.email())){
            throw UserAlreadyExistException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        String profileImageUrl = request.profileImageUrl();

        User user = new User(
                null,
                request.email(),
                encodedPassword,
                request.name(),
                profileImageUrl != null && !profileImageUrl.isEmpty()
                        ? profileImageUrl
                        : defaultImageProperties.defaultImageUrl(),
                request.gender(),
                Role.USER,
                DEFAULT_PROVIDER,
                request.fcmToken(),
                null
        );

        userSavePort.save(user);

        if(request.fcmToken() != null){
            initializeTopicsUseCase.execute(user.email(), request.fcmToken(), request.isEventAgreed());
        }
    }
}
