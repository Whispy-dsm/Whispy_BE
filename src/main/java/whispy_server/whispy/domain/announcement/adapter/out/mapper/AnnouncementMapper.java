package whispy_server.whispy.domain.announcement.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnnouncementMapper {

    Announcement toModel(AnnouncementJpaEntity entity);
    AnnouncementJpaEntity toEntity(Announcement model);

    List<Announcement> toModelList(List<AnnouncementJpaEntity> announcementJpaEntities);

    default Optional<Announcement> toOptionalModel(Optional<AnnouncementJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
