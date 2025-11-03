package whispy_server.whispy.domain.sleepsession.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.sleepsession.adapter.out.entity.SleepSessionJpaEntity;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SleepSessionMapper {

    SleepSession toModel(SleepSessionJpaEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    SleepSessionJpaEntity toEntity(SleepSession domain);

    default Optional<SleepSession> toOptionalModel(Optional<SleepSessionJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    default Page<SleepSession> toModelPage(Page<SleepSessionJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
