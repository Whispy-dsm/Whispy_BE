package whispy_server.whispy.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.domain.auth.application.port.in.CheckEmailVerificationUseCase;
import whispy_server.whispy.global.utils.redis.RedisUtil;

/**
 * 이메일 인증 상태 확인 서비스
 *
 * 이메일의 인증 완료 여부를 조회하는 유스케이스 구현체입니다.
 * Redis에 저장된 인증 상태 값을 조회하여 인증 완료 여부를 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class CheckEmailVerificationService implements CheckEmailVerificationUseCase {

    private final RedisUtil redisUtil;

    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";

    /**
     * 이메일 인증 상태를 확인합니다.
     *
     * Redis에서 이메일의 인증 상태를 조회합니다.
     * 상태 값이 "verified"인 경우 true, 그렇지 않으면 false를 반환합니다.
     *
     * @param request 인증 상태를 확인할 이메일 주소가 포함된 요청
     * @return 인증 완료 여부가 포함된 응답
     */
    @Override
    public CheckEmailVerificationResponse execute(CheckEmailVerificationRequest request) {
        String statusKey = VERIFICATION_STATUS_KEY + request.email();
        String status = redisUtil.get(statusKey);

        return new CheckEmailVerificationResponse("verified".equals(status));
    }
}
