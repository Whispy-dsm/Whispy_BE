package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.domain.auth.application.port.in.SendEmailVerificationUseCase;
import whispy_server.whispy.domain.auth.application.port.out.EmailSendPort;
import whispy_server.whispy.global.exception.domain.auth.EmailAlreadySentException;
import whispy_server.whispy.global.exception.domain.auth.EmailRateLimitExceededException;
import whispy_server.whispy.global.exception.domain.auth.EmailSendFailedException;
import whispy_server.whispy.global.utils.redis.RedisUtil;
import whispy_server.whispy.global.annotation.UserAction;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * 이메일 인증 코드 발송 서비스
 *
 * 이메일로 인증 코드를 발송하는 유스케이스 구현체입니다.
 * 6자리 인증 코드를 생성하여 이메일로 발송하며, Rate limit 및 중복 요청 검증을 수행합니다.
 * Redis를 사용하여 인증 코드와 발송 이력을 관리합니다.
 */
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

    /**
     * 이메일 인증 코드를 발송합니다.
     *
     * Rate limit 검증(1분) 및 중복 요청 검증(5분)을 수행한 후,
     * 6자리 인증 코드를 생성하여 이메일로 발송합니다.
     * 발송 실패 시 Redis에 저장된 데이터를 롤백합니다.
     *
     * @param request 인증 코드를 받을 이메일 주소가 포함된 요청
     * @throws EmailRateLimitExceededException Rate limit 초과 시
     * @throws EmailAlreadySentException 이미 발송된 인증 코드가 존재할 시
     * @throws EmailSendFailedException 이메일 발송 실패 시
     */
    @Override
    @UserAction("이메일 인증 코드 발송")
    public void execute(SendEmailVerificationRequest request) {
        String email = request.email();
        String code = generateVerificationCode();
        String codeKey = VERIFICATION_CODE_KEY + email;
        String rateLimitKey = RATE_LIMIT_KEY + email;

        if (!redisUtil.setIfAbsent(rateLimitKey, "sent", RATE_LIMIT_DURATION)) {
            throw EmailRateLimitExceededException.EXCEPTION;
        }

        if (!redisUtil.setIfAbsent(codeKey, code, CODE_EXPIRATION)) {
            redisUtil.delete(rateLimitKey);
            throw EmailAlreadySentException.EXCEPTION;
        }

        try {
            emailSendPort.sendVerificationCode(email, code);
        } catch (Exception e) {
            redisUtil.delete(codeKey);
            redisUtil.delete(rateLimitKey);
            throw new EmailSendFailedException(e);
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

    /**
     * 6자리 숫자 인증 코드를 생성합니다.
     */
    private String generateVerificationCode() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
