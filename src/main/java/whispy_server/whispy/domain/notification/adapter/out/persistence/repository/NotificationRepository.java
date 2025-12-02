package whispy_server.whispy.domain.notification.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;

import java.util.List;

/**
 * 알림 레포지토리.
 *
 * NotificationJpaEntity에 대한 데이터베이스 접근을 담당하는 Spring Data JPA 레포지토리입니다.
 */
public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    /**
     * 이메일로 알림 목록을 생성일시 내림차순으로 페이지네이션하여 조회합니다.
     * EntityGraph를 사용하여 data 컬렉션을 함께 조회합니다.
     *
     * @param email 사용자 이메일
     * @param pageable 페이지 정보
     * @return 알림 엔티티 페이지
     */
    @EntityGraph(attributePaths = {"data"})
    Page<NotificationJpaEntity> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable);

    /**
     * 이메일로 읽지 않은 알림 목록을 생성일시 내림차순으로 조회합니다.
     * EntityGraph를 사용하여 data 컬렉션을 함께 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 엔티티 목록
     */
    @EntityGraph(attributePaths = {"data"})
    List<NotificationJpaEntity> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);

    /**
     * 이메일로 읽지 않은 알림 목록을 조회합니다.
     * EntityGraph를 사용하여 data 컬렉션을 함께 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 엔티티 목록
     */
    @EntityGraph(attributePaths = {"data"})
    List<NotificationJpaEntity> findByEmailAndReadFalse(String email);

    /**
     * 이메일로 읽지 않은 알림 개수를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 개수
     */
    int countByEmailAndReadFalse(String email);

    /**
     * 이메일로 해당 사용자의 모든 알림을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(String email);
}
