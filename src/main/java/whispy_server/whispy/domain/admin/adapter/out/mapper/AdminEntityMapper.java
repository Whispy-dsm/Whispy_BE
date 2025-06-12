package whispy_server.whispy.domain.admin.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.admin.adapter.out.entity.AdminJpaEntity;
import whispy_server.whispy.domain.admin.model.Admin;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminEntityMapper {

    Admin toDomain(AdminJpaEntity entity);
    AdminJpaEntity toEntity(Admin adminDomain);
}
