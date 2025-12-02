package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.domain.auth.application.port.in.VerifyEmailCodeUseCase;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import java.time.Duration;

/**
 * 이메일 인증 코드 검증 서비스 구현체.
 *
 * Redis에 저장된 코드를 비교해 일치 여부를 판단하고 인증 상태를 기록한다.
 */
@Service
@RequiredArgsConstructor
public class VerifyEmailCodeService implements VerifyEmailCodeUseCase {

    private final RedisUtil redisUtil;

    private static final String VERIFICATION_CODE_KEY = "email:verification:code:";
    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";
    private static final Duration STATUS_EXPIRATION = Duration.ofMinutes(10);

    /**
     * 이메일 인증 코드를 검증한다.
     *
     * @param request 이메일·코드 정보
     * @return 인증 성공 여부 응답
     */
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
