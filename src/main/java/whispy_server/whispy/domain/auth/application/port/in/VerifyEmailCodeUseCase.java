package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 이메일 인증 코드 검증 유스케이스
 *
 * 사용자가 입력한 인증 코드를 검증하는 인바운드 포트입니다.
 * Redis에 저장된 인증 코드와 일치 여부를 확인하고, 성공 시 인증 상태를 저장합니다.
 */
@UseCase
public interface VerifyEmailCodeUseCase {
    /**
     * 이메일 인증 코드를 검증합니다.
     *
     * @param request 이메일 주소와 인증 코드가 포함된 요청
     * @return 인증 성공 여부가 포함된 응답
     */
    VerifyEmailCodeResponse execute(VerifyEmailCodeRequest request);
}
