package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.domain.user.adapter.in.web.dto.request.ChangeProfileRequest;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 프로필 변경 유스케이스.
 * 사용자의 프로필 정보(이름, 이미지, 성별)를 변경합니다.
 */
@UseCase
public interface ChangeProfileUseCase {
    /**
     * 사용자의 프로필 정보를 변경합니다.
     *
     * @param request 프로필 변경 요청 (이름, 프로필 이미지 URL, 성별)
     */
    void execute(ChangeProfileRequest request);
}
