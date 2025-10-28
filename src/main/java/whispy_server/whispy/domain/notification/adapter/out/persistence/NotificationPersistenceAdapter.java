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

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationEntityMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Notification> findById(Long id){
        return mapper.toOptionalModel(notificationRepository.findById(id));
    }

    @Override
    public Page<Notification> findByEmailOrderByCreatedAtDesc(String email, Pageable pageable){
        return mapper.toModelPage(notificationRepository.findByEmailOrderByCreatedAtDesc(email, pageable));
    }

    @Override
    public List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email){
        return mapper.toModelList(notificationRepository.findByEmailAndReadFalseOrderByCreatedAtDesc(email));
    }

    @Override
    public List<Notification> findByEmailAndIsReadFalse(String email){
        return mapper.toModelList(notificationRepository.findByEmailAndReadFalse(email));
    }

    @Override
    public int countByEmailAndIsReadFalse(String email){
        return notificationRepository.countByEmailAndReadFalse(email);
    }

    @Override
    public void save(Notification notification){
        notificationRepository.save(mapper.toEntity(notification));
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        notificationRepository.saveAll(mapper.toEntityList(notifications));
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void deleteAllByEmail(String email) {
        notificationRepository.deleteAllByEmail(email);
    }

    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        QNotificationJpaEntity notification = QNotificationJpaEntity.notificationJpaEntity;

            jpaQueryFactory
                .delete(notification)
                .where(notification.id.in(ids))
                .execute();
    }
}
