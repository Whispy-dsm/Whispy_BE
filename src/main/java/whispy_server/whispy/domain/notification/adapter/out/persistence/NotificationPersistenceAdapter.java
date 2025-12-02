package whispy_server.whispy.domain.notification.adapter.out.persistence;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.adapter.out.entity.QNotificationJpaEntity;
import whispy_server.whispy.domain.notification.adapter.out.mapper.NotificationEntityMapper;
import whispy_server.whispy.domain.notification.adapter.out.persistence.repository.NotificationRepository;
import whispy_server.whispy.domain.notification.application.port.out.NotificationPort;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

/**
 * 알림 영속성 어댑터.
 *
 * 알림 도메인의 영속성 작업을 처리하는 아웃바운드 어댑터입니다.
 * NotificationRepository와 QueryDSL을 사용하여 데이터베이스 작업을 수행합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationEntityMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * ID로 알림을 조회합니다.
     *
     * @param id 알림 ID
     * @return 알림 Optional
     */
    @Override
    public Optional<Notification> findById(Long id){
        return mapper.toOptionalModel(notificationRepository.findById(id));
    }

    /**
     * 이메일로 알림 목록을 생성일시 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param email 사용자 이메일
     * @param pageable 페이지 정보
     * @return 알림 목록 페이지
     */
    @Override
    public Page<Notification> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable){
        return mapper.toModelPage(notificationRepository.findByEmailOrderByCreatedAtDesc(email, pageable));
    }

    /**
     * 이메일로 읽지 않은 알림 목록을 생성일시 내림차순으로 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 목록
     */
    @Override
    public List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email){
        return mapper.toModelList(notificationRepository.findByEmailAndReadFalseOrderByCreatedAtDesc(email));
    }

    /**
     * 이메일로 읽지 않은 알림 목록을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 목록
     */
    @Override
    public List<Notification> findByEmailAndIsReadFalse(String email){
        return mapper.toModelList(notificationRepository.findByEmailAndReadFalse(email));
    }

    /**
     * 이메일로 읽지 않은 알림 개수를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 읽지 않은 알림 개수
     */
    @Override
    public int countByEmailAndIsReadFalse(String email){
        return notificationRepository.countByEmailAndReadFalse(email);
    }

    /**
     * 알림을 저장합니다.
     *
     * @param notification 저장할 알림
     */
    @Override
    public void save(Notification notification){
        notificationRepository.save(mapper.toEntity(notification));
    }

    /**
     * 여러 알림을 일괄 저장합니다.
     *
     * @param notifications 저장할 알림 목록
     */
    @Override
    public void saveAll(List<Notification> notifications) {
        notificationRepository.saveAll(mapper.toEntityList(notifications));
    }

    /**
     * ID로 알림을 삭제합니다.
     *
     * @param id 삭제할 알림 ID
     */
    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    /**
     * 이메일로 해당 사용자의 모든 알림을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    @Override
    public void deleteByEmail(String email) {
        notificationRepository.deleteByEmail(email);
    }

    /**
     * 여러 알림을 배치로 삭제합니다.
     *
     * @param ids 삭제할 알림 ID 목록
     */
    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        QNotificationJpaEntity notification = QNotificationJpaEntity.notificationJpaEntity;

            jpaQueryFactory
                .delete(notification)
                .where(notification.id.in(ids))
                .execute();
    }
}
