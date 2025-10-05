package whispy_server.whispy.domain.notification.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.notification.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {

    NotificationJpaEntity toEntity(Notification notification);
    Notification toModel(NotificationJpaEntity notificationJpaEntity);

    List<Notification> toModelList(List<NotificationJpaEntity> notificationJpaEntityList);
    List<NotificationJpaEntity> toEntityList(List<Notification> notifications);

    default Optional<Notification> toOptionalModel(Optional<NotificationJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    default Page<Notification> toModelPage(Page<NotificationJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
