package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.domain.auth.application.port.in.VerifyEmailCodeUseCase;
import whispy_server.whispy.global.redis.RedisUtil;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class VerifyEmailCodeService implements VerifyEmailCodeUseCase {

    private final RedisUtil redisUtil;

    private static final String VERIFICATION_CODE_KEY = "email:verification:code:";
    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";
    private static final Duration STATUS_EXPIRATION = Duration.ofHours(24);

    @Override
    public VerifyEmailCodeResponse execute(VerifyEmailCodeRequest request) {
        String codeKey = VERIFICATION_CODE_KEY + request.email();
        String storedCode = redisUtil.get(codeKey);

        if (storedCode == null) {
            return new VerifyEmailCodeResponse(false);
        }

        if (!storedCode.equals(request.code())) {
            return new VerifyEmailCodeResponse(false);
        }

        String statusKey = VERIFICATION_STATUS_KEY + request.email();
        redisUtil.set(statusKey, "verified", STATUS_EXPIRATION);
        redisUtil.delete(codeKey);

        return new VerifyEmailCodeResponse(true);
    }
}
