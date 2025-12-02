package whispy_server.whispy.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangePasswordRequest;
import whispy_server.whispy.domain.user.application.port.in.ChangePasswordUseCase;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.application.port.out.UserSavePort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.auth.EmailNotVerifiedException;
import whispy_server.whispy.global.exception.domain.user.PasswordMissMatchException;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;
import whispy_server.whispy.global.utils.redis.RedisUtil;

/**
 * 비밀번호 변경 서비스.
 * 이메일 인증을 거친 사용자의 비밀번호를 변경합니다.
 */
@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    private final static String VERIFICATION_STATUS_KEY = "email:verification:status:";

    /**
     * 사용자의 비밀번호를 변경합니다.
     * Redis에서 이메일 인증 상태를 확인한 후 비밀번호를 변경하고 인증 상태를 삭제합니다.
     *
     * @param request 비밀번호 변경 요청 (이메일, 새 비밀번호)
     */
    @Override
    @Transactional
    public void execute(ChangePasswordRequest request) {
        String statusKey = VERIFICATION_STATUS_KEY + request.email();
        String status = redisUtil.get(statusKey);

        User currentUser = queryUserPort.findByEmail(request.email())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!"verified".equals(status)) {
            throw EmailNotVerifiedException.EXCEPTION;
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        User updatedUser = currentUser.updatePassword(encodedNewPassword);

        userSavePort.save(updatedUser);
        redisUtil.delete(statusKey);
    }
}
