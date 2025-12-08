package whispy_server.whispy.domain.like.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.like.adapter.out.entity.MusicLikeJpaEntity;
import whispy_server.whispy.domain.like.model.MusicLike;

import java.util.List;
import java.util.Optional;

/**
 * MusicLike 도메인 모델과 JPA 엔티티 간 변환을 담당하는 MapStruct 매퍼.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MusicLikeMapper {

    MusicLike toModel(MusicLikeJpaEntity entity);
    MusicLikeJpaEntity toEntity(MusicLike musicLike);
    List<MusicLike> toModelList(List<MusicLikeJpaEntity> entities);

    default Optional<MusicLike> toOptionalModel(Optional<MusicLikeJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }
}
