package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 이메일 인증 코드 발송 유스케이스
 *
 * 이메일로 인증 코드를 발송하는 인바운드 포트입니다.
 * 6자리 인증 코드를 생성하여 이메일로 발송하며, Rate limit 및 중복 요청 검증을 수행합니다.
 */
@UseCase
public interface SendEmailVerificationUseCase {
    /**
     * 이메일 인증 코드를 발송합니다.
     *
     * @param request 인증 코드를 받을 이메일 주소가 포함된 요청
     */
    void execute(SendEmailVerificationRequest request);
}
