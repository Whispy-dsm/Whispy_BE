package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.InitializeTopicsRequest;
import whispy_server.whispy.domain.topic.application.port.in.InitializeTopicsUseCase;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
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

/**
 * 사용자 로그인 서비스.
 * 이메일과 비밀번호를 사용한 로컬 인증을 처리하고 JWT 토큰을 발급합니다.
 */
@Service
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;
    private final InitializeTopicsUseCase initializeTopicsUseCase;
    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 사용자 로그인을 처리합니다.
     * 비밀번호를 검증하고, FCM 토큰을 업데이트하며, 기존 세션을 무효화한 후 새로운 JWT 토큰을 발급합니다.
     *
     * @param request 로그인 요청 (이메일, 비밀번호, FCM 토큰)
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    @Transactional
    @Override
    public TokenResponse login(UserLoginRequest request) {
        User user = authenticatedUser(request);

        // (기존 세션 무효화)
        refreshTokenRepository.deleteById(user.id());


        return generateToken(user.id());
    }

    /**
     * 사용자 인증을 수행하고 FCM 토큰을 업데이트합니다.
     *
     * 인증 로직:
     * 1. 이메일로 사용자 조회
     * 2. 비밀번호 일치 여부 확인
     * 3. FCM 토큰이 변경되었으면:
     *    - 이전 기기에 로그아웃 알림 전송
     *    - 새 FCM 토큰으로 업데이트
     *    - 새 기기 토픽 초기화
     *
     * @param request 로그인 요청 (이메일, 비밀번호, FCM 토큰)
     * @return 인증된 사용자 (FCM 토큰이 업데이트된 상태)
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     * @throws PasswordMissMatchException 비밀번호가 일치하지 않는 경우
     */
    private User authenticatedUser(UserLoginRequest request) {
        User user = queryUserPort.findByEmail(request.email())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw PasswordMissMatchException.EXCEPTION;
        }

        if (request.fcmToken() != null && !request.fcmToken().equals(user.fcmToken())) {

            if (user.fcmToken() != null && !user.fcmToken().trim().isEmpty()) {
                sendLogoutNotification(user.fcmToken(), user.email());
            }

            User updatedUser = user.updateFcmToken(request.fcmToken());
            userSavePort.save(updatedUser);

            // 새 기기 토픽 초기화
            initializeTopicsUseCase.execute(
                    new InitializeTopicsRequest(user.email(), request.fcmToken(), false)
            );

            return updatedUser;
        }

        return user;
    }

    /**
     * 이전 기기에 로그아웃 알림을 전송합니다.
     *
     * 새로운 기기에서 로그인할 때, 기존 기기에 푸시 알림을 보내
     * 다른 기기에서 로그인하여 자동 로그아웃되었음을 알립니다.
     *
     * 알림 전송 실패 시에도 예외를 던지지 않고 로그인은 계속 진행합니다.
     *
     * @param oldToken 이전 기기의 FCM 토큰
     * @param email    사용자 이메일
     */
    private void sendLogoutNotification(String oldToken, String email) {
        try {
            NotificationSendRequest request = new NotificationSendRequest(
                    email,
                    List.of(oldToken),
                    NotificationTopic.SYSTEM_ANNOUNCEMENT,
                    "로그아웃",
                    "다른 기기에서 로그인하여 자동 로그아웃 되었습니다.",
                    null
            );

            sendToDeviceTokensUseCase.execute(request);
        } catch (Exception e) {
            // 알림 전송 실패해도 로그인은 계속 진행
            // 로그만 남김
        }
    }

    /**
     * JWT 액세스 토큰과 리프레시 토큰을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return JWT 토큰 응답 (액세스 토큰 + 리프레시 토큰)
     */
    private TokenResponse generateToken(Long userId) {
        return jwtTokenProvider.generateToken(userId, Role.USER.toString());
    }
}