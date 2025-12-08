package whispy_server.whispy.domain.notification.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

/**
 * 알림 조회 아웃바운드 포트.
 *
 * 알림을 조회하는 영속성 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface QueryNotificationPort {
    /**
     * ID로 알림을 조회합니다.
     *
     * @param notificationId 알림 ID
     * @return 알림 Optional
     */
    Optional<Notification> findById(Long notificationId);

    /**
     * 이메일로 알림 목록을 생성일시 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param email 사용자 이메일
     * @param pageable 페이지 정보
     * @return 알림 목록 페이지
     */
    Page<Notification> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable);

    /**
     * 이메일로 읽지 않은 알림 목록을 생성일시 내림차순으로 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 목록
     */
    List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);

    /**
     * 이메일로 읽지 않은 알림 목록을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 목록
     */
    List<Notification> findByEmailAndIsReadFalse(String email);

    /**
     * 이메일로 읽지 않은 알림 개수를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 개수
     */
    int countByEmailAndIsReadFalse(String email);

}
