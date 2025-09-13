package whispy_server.whispy.domain.notification.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
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
        Optional<NotificationJpaEntity> optionalEntity = notificationRepository.findById(id);
        return mapper.toOptionalModel(optionalEntity);
    }

    @Override
    public List<Notification> findByEmailOrderByCreatedAtDesc(String email){
        List<NotificationJpaEntity> entities = notificationRepository.findByEmailOrderByCreatedAtDesc(email);
        return mapper.toModelList(entities);
    }

    @Override
    public List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email){
        List<NotificationJpaEntity> entities = notificationRepository.findByEmailAndReadFalseOrderByCreatedAtDesc(email);
        return mapper.toModelList(entities);
    }

    @Override
    public List<Notification> findByEmailAndIsReadFalse(String email){
        List<NotificationJpaEntity> entities = notificationRepository.findByEmailAndReadFalse(email);
        return mapper.toModelList(entities);
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

}
