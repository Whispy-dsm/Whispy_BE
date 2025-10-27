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

@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final QueryUserPort queryUserPort;
    private final UserSavePort userSavePort;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    private final static String VERIFICATION_STATUS_KEY = "email:verification:status:";

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
