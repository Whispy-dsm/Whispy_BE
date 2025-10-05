package whispy_server.whispy.domain.search.music.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.search.music.adapter.out.entity.MusicElasticsearchEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MusicElasticMapper {

    @Mapping(target = "id", expression = "java(music.id() != null ? music.id().toString() : null)")
    @Mapping(target = "category", expression = "java(music.category().name())")
    MusicElasticsearchEntity toElasticsearchEntity(Music music);

    @Mapping(target = "id", expression = "java(entity.getId() != null ? Long.valueOf(entity.getId()) : null)")
    @Mapping(target = "category", expression = "java(whispy_server.whispy.domain.music.model.type.MusicCategory.valueOf(entity.getCategory()))")
    Music toModel(MusicElasticsearchEntity entity);

    default Page<Music> toMusicPage(Page<MusicElasticsearchEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
