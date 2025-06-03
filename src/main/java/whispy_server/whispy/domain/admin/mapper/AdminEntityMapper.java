package whispy_server.whispy.domain.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;
import whispy_server.whispy.domain.admin.domain.Admin;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;
import whispy_server.whispy.domain.user.domain.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminEntityMapper {

    Admin toDomain(AdminJpaEntity entity);
    AdminJpaEntity toEntity(Admin adminDomain);
}
