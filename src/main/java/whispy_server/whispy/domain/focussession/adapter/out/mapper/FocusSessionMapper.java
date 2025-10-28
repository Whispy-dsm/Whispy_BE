package whispy_server.whispy.domain.focussession.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;
import whispy_server.whispy.domain.focussession.model.FocusSession;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FocusSessionMapper {

    FocusSession toModel(FocusSessionJpaEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    FocusSessionJpaEntity toEntity(FocusSession domain);
}
