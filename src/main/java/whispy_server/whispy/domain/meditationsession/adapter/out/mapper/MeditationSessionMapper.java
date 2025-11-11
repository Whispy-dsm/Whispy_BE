package whispy_server.whispy.domain.meditationsession.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.meditationsession.adapter.out.entity.MeditationSessionJpaEntity;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeditationSessionMapper {

    MeditationSession toModel(MeditationSessionJpaEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    MeditationSessionJpaEntity toEntity(MeditationSession domain);

    default Optional<MeditationSession> toOptionalModel(Optional<MeditationSessionJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    default Page<MeditationSession> toModelPage(Page<MeditationSessionJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
