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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationEntityMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Notification> findById(UUID id){
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
    public Notification save(Notification notification){
        NotificationJpaEntity entity = mapper.toEntity(notification);
        NotificationJpaEntity saveEntity = notificationRepository.save(entity);
        return mapper.toModel(saveEntity);
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        List<NotificationJpaEntity> entities = mapper.toEntityList(notifications);
        List<NotificationJpaEntity> savedEntities = notificationRepository.saveAll(entities);
        return mapper.toModelList(savedEntities);
    }

}
