package whispy_server.whispy.domain.notification.application.port.out;

import java.util.List;

/**
 * 알림 삭제 아웃바운드 포트.
 *
 * 알림을 삭제하는 영속성 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface DeleteNotificationPort {
    /**
     * ID로 알림을 삭제합니다.
     *
     * @param id 삭제할 알림 ID
     */
    void deleteById(Long id);

    /**
     * 이메일로 해당 사용자의 모든 알림을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(String email);

    /**
     * 여러 알림을 배치로 삭제합니다.
     *
     * @param ids 삭제할 알림 ID 목록
     */
    void deleteAllByIdInBatch(List<Long> ids);
}
