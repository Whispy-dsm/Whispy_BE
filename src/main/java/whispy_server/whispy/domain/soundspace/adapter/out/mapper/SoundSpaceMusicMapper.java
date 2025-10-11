package whispy_server.whispy.domain.soundspace.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.soundspace.adapter.out.entity.SoundSpaceMusicJpaEntity;
import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SoundSpaceMusicMapper {

    SoundSpaceMusic toModel(SoundSpaceMusicJpaEntity entity);
    SoundSpaceMusicJpaEntity toEntity(SoundSpaceMusic model);

    List<SoundSpaceMusic> toModelList(List<SoundSpaceMusicJpaEntity> entities);
}
