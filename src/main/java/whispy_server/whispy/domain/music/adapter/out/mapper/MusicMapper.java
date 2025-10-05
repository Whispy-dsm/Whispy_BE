package whispy_server.whispy.domain.music.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.user.model.User;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MusicMapper {
    
    Music toModel(MusicJpaEntity musicJpaEntity);

    MusicJpaEntity toEntity(Music music);

    default Optional<Music> toOptionalModel(Optional<MusicJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }

    default Page<Music> toPageModel(Page<MusicJpaEntity> pageEntity){
        return pageEntity.map(this::toModel);
    }
}

