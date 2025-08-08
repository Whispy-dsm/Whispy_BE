package whispy_server.whispy.domain.fcm.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.fcm.adapter.out.entity.NotificationJpaEntity;
import whispy_server.whispy.domain.fcm.model.Notification;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {

    NotificationJpaEntity toEntity(Notification notification);
    Notification toModel(NotificationJpaEntity notificationJpaEntity);

    default Optional<Notification> toOptionalModel(Optional<NotificationJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }
}
