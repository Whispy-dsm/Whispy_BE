package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ResetPasswordRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 비밀번호 재설정 유스케이스.
 * 이메일 인증을 통해 비밀번호를 잊어버린 경우 재설정합니다.
 */
@UseCase
public interface ResetPasswordUseCase {
    /**
     * 비밀번호를 재설정합니다.
     * 이메일 인증이 완료된 상태에서 호출되어야 합니다.
     *
     * @param request 비밀번호 재설정 요청 (이메일, 새 비밀번호)
     */
    void execute(ResetPasswordRequest request);
}
