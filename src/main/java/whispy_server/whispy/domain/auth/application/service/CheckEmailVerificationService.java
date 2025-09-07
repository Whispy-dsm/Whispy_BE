package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.domain.auth.application.port.in.CheckEmailVerificationUseCase;
import whispy_server.whispy.global.redis.RedisUtil;

@Service
@RequiredArgsConstructor
public class CheckEmailVerificationService implements CheckEmailVerificationUseCase {

    private final RedisUtil redisUtil;

    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";

    @Override
    public CheckEmailVerificationResponse execute(CheckEmailVerificationRequest request) {
        String statusKey = VERIFICATION_STATUS_KEY + request.email();
        String status = redisUtil.get(statusKey);
        
        return new CheckEmailVerificationResponse("verified".equals(status));
    }
}
