package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.RegisterRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자 회원가입 유스케이스.
 * 로컬 계정으로 새로운 사용자를 등록합니다.
 */
@UseCase
public interface UserRegisterUseCase {

    /**
     * 새로운 사용자를 회원가입 처리합니다.
     *
     * @param registerRequest 회원가입 요청 (이메일, 비밀번호, 이름 등)
     */
    void register(RegisterRequest registerRequest);
}
