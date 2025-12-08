package whispy_server.whispy.domain.reason.application.port.out;

/**
 * 탈퇴 사유 삭제 Port.
 */
public interface WithdrawalReasonDeletePort {
    /**
     * 탈퇴 사유를 삭제한다.
     */
    void deleteById(Long id);
}
