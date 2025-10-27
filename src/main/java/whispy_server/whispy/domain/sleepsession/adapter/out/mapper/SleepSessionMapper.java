package whispy_server.whispy.domain.sleepsession.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.SleepSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SleepSessionMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    SleepSession toModel(SleepSessionJpaEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    SleepSessionJpaEntity toEntity(SleepSession domain);
}
