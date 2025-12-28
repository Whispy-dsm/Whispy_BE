package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ResetPasswordRequest;
import whispy_server.whispy.domain.user.application.port.in.ResetPasswordUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.auth.EmailNotVerifiedException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.utils.redis.RedisUtil;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 비밀번호 재설정 서비스.
 * 비밀번호를 잊어버린 사용자가 이메일 인증을 통해 비밀번호를 재설정합니다.
 */
@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";

    /**
     * 비밀번호를 재설정합니다.
     * Redis에서 이메일 인증 상태를 확인한 후 비밀번호를 재설정하고 인증 상태를 삭제합니다.
     *
     * @param request 비밀번호 재설정 요청 (이메일, 새 비밀번호)
     */
    @Override
    @Transactional
    @UserAction("비밀번호 재설정")
    public void execute(ResetPasswordRequest request) {
        String statusKey = VERIFICATION_STATUS_KEY + request.email();
        String status = redisUtil.get(statusKey);

        if (!"verified".equals(status)) {
            throw EmailNotVerifiedException.EXCEPTION;
        }

        User user = queryUserPort.findByEmail(request.email())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        User updatedUser = user.updatePassword(encodedNewPassword);

        userSavePort.save(updatedUser);
        redisUtil.delete(statusKey);
    }
}
