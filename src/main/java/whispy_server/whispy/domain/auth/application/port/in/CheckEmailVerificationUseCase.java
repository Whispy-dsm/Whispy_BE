package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 이메일 인증 상태 확인 유스케이스
 *
 * 이메일의 인증 완료 여부를 조회하는 인바운드 포트입니다.
 * Redis에 저장된 인증 상태를 확인하여 결과를 반환합니다.
 */
@UseCase
public interface CheckEmailVerificationUseCase {
    /**
     * 이메일 인증 상태를 확인합니다.
     *
     * @param request 인증 상태를 확인할 이메일 주소가 포함된 요청
     * @return 인증 완료 여부가 포함된 응답
     */
    CheckEmailVerificationResponse execute(CheckEmailVerificationRequest request);
}
