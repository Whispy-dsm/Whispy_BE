package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.domain.auth.application.port.in.SendEmailVerificationUseCase;
import whispy_server.whispy.domain.auth.application.port.out.EmailSendPort;
import whispy_server.whispy.global.exception.domain.auth.EmailAlreadySentException;
import whispy_server.whispy.global.exception.domain.auth.EmailRateLimitExceededException;
import whispy_server.whispy.global.exception.domain.auth.EmailSendFailedException;
import whispy_server.whispy.global.redis.RedisUtil;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SendEmailVerificationService implements SendEmailVerificationUseCase {

    private final RedisUtil redisUtil;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmailSendPort emailSendPort;

    private static final String VERIFICATION_CODE_KEY = "email:verification:code:";
    private static final String RATE_LIMIT_KEY = "email:rate:limit:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration RATE_LIMIT_DURATION = Duration.ofMinutes(1);

    @Override
    public void execute(SendEmailVerificationRequest request) {
        String email = request.email();
        String code = generateVerificationCode();
        String codeKey = VERIFICATION_CODE_KEY + email;
        String rateLimitKey = RATE_LIMIT_KEY + email;

        if (!redisUtil.setIfAbsent(rateLimitKey, "sent", RATE_LIMIT_DURATION)) {
            throw EmailRateLimitExceededException.EXCEPTION;
        }
        
        try {
            if (!redisUtil.setIfAbsent(codeKey, code, CODE_EXPIRATION)) {
                throw EmailAlreadySentException.EXCEPTION;
            }

            emailSendPort.sendVerificationCode(email, code);
            
        } catch (Exception e) {
            // 발송 실패 시 Redis 롤백
            redisUtil.delete(codeKey);
            redisUtil.delete(rateLimitKey);
            throw EmailSendFailedException.EXCEPTION;
        }
    }

//    private void validateRateLimit(String email) {
//        String rateLimitKey = RATE_LIMIT_KEY + email;
//        if (redisUtil.hasKey(rateLimitKey)) {
//            throw EmailRateLimitExceededException.EXCEPTION;
//        }
//    }
//
//    private void validateDuplicateRequest(String email) {
//        String codeKey = VERIFICATION_CODE_KEY + email;
//        if (redisUtil.hasKey(codeKey)) {
//            throw EmailAlreadySentException.EXCEPTION;
//        }
//    }

    private String generateVerificationCode() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
