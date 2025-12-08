package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangePasswordRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 비밀번호 변경 유스케이스.
 * 인증된 사용자가 자신의 비밀번호를 변경합니다.
 */
@UseCase
public interface ChangePasswordUseCase {
    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param request 비밀번호 변경 요청 (이메일, 새 비밀번호)
     */
    void execute(ChangePasswordRequest request);
}
