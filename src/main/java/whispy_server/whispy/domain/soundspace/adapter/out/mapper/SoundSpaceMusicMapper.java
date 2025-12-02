package whispy_server.whispy.domain.soundspace.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.soundspace.adapter.out.entity.SoundSpaceMusicJpaEntity;
import whispy_server.whispy.domain.soundspace.model.SoundSpaceMusic;

import java.util.List;

/**
 * 사운드 스페이스 엔티티와 도메인 모델 간 변환을 담당하는 MapStruct 매퍼.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SoundSpaceMusicMapper {

    SoundSpaceMusic toModel(SoundSpaceMusicJpaEntity entity);
    SoundSpaceMusicJpaEntity toEntity(SoundSpaceMusic model);
    List<SoundSpaceMusic> toModelList(List<SoundSpaceMusicJpaEntity> entities);
}
