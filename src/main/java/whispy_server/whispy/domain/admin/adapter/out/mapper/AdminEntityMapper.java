package whispy_server.whispy.domain.admin.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;
import whispy_server.whispy.domain.admin.model.Admin;
import whispy_server.whispy.domain.announcement.adapter.out.entity.AnnouncementJpaEntity;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminEntityMapper {

    Admin toModel(AdminJpaEntity entity);
    AdminJpaEntity toEntity(Admin adminDomain);

    default Optional<Admin> toOptionalModel(Optional<AdminJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }
}
