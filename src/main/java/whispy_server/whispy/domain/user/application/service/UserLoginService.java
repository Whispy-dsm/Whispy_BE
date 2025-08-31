package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.auth.adapter.out.entity.types.Role;
import whispy_server.whispy.domain.auth.adapter.out.persistence.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.application.port.in.UserLoginUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.security.jwt.JwtTokenProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmSendPort fcmSendPort;

    @Transactional
    @Override
    public TokenResponse login(UserLoginRequest request) {
        User user = authenticatedUser(request);

        // (기존 세션 무효화)
        refreshTokenRepository.deleteById(user.email());


        return generateToken(user.email());
    }

    private User authenticatedUser(UserLoginRequest request) {
        User user = queryUserPort.findByEmail(request.email())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw PasswordMissMatchException.EXCEPTION;
        }

        if (request.fcmToken() != null && !request.fcmToken().equals(user.fcmToken())) {

            if (user.fcmToken() != null && !user.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(user.fcmToken());
            }

            User updatedUser = user.updateFcmToken(request.fcmToken());
            userSavePort.save(updatedUser);

            // 새 기기 토픽 초기화
            initializeTopicsUseCase.execute(user.email(), request.fcmToken());

            return updatedUser;
        }

        return user;
    }

    private void sendLogoutNotification(String oldToken) {
        try {
            fcmSendPort.sendMulticast(
                    List.of(oldToken),
                    "로그아웃",
                    "다른 기기에서 로그인하여 자동 로그아웃되었습니다.",
                    null
            );
        } catch (Exception e) {
            // 알림 전송 실패해도 로그인은 계속 진행
            // 로그만 남김
        }
    }

    private TokenResponse generateToken(String email) {
        return jwtTokenProvider.generateToken(email, Role.USER.toString());
    }
}