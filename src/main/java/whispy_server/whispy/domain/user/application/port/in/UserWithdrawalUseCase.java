package whispy_server.whispy.domain.user.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자 회원 탈퇴 유스케이스.
 * 현재 인증된 사용자의 계정을 삭제합니다.
 */
@UseCase
public interface UserWithdrawalUseCase {

    /**
     * 현재 인증된 사용자의 계정을 삭제합니다.
     * 관련된 모든 데이터가 함께 삭제됩니다.
     */
    void withdrawal();
}
