package whispy_server.whispy.domain.music.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.music.adapter.out.entity.MusicJpaEntity;
import whispy_server.whispy.domain.music.model.Music;

import java.util.Optional;

/**
 * 음악 매퍼 인터페이스.
 *
 * MusicJpaEntity와 Music 도메인 모델 간의 변환을 담당하는 MapStruct 매퍼입니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MusicMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param musicJpaEntity 음악 JPA 엔티티
     * @return 음악 도메인 모델
     */
    Music toModel(MusicJpaEntity musicJpaEntity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param music 음악 도메인 모델
     * @return 음악 JPA 엔티티
     */
    MusicJpaEntity toEntity(Music music);

    /**
     * Optional JPA 엔티티를 Optional 도메인 모델로 변환합니다.
     *
     * @param optionalEntity Optional 음악 JPA 엔티티
     * @return Optional 음악 도메인 모델
     */
    default Optional<Music> toOptionalModel(Optional<MusicJpaEntity> optionalEntity){
        return optionalEntity.map(this::toModel);
    }

    /**
     * Page JPA 엔티티를 Page 도메인 모델로 변환합니다.
     *
     * @param pageEntity 음악 JPA 엔티티 페이지
     * @return 음악 도메인 모델 페이지
     */
    default Page<Music> toPageModel(Page<MusicJpaEntity> pageEntity){
        return pageEntity.map(this::toModel);
    }
}
