package whispy_server.whispy.domain.focussession.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.focussession.adapter.out.entity.FocusSessionJpaEntity;
import whispy_server.whispy.domain.focussession.model.FocusSession;

import java.util.Optional;

/**
 * 집중 세션 엔티티-도메인 모델 매퍼.
 *
 * JPA 엔티티와 도메인 모델 간의 양방향 변환을 담당하는 MapStruct 매퍼입니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FocusSessionMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param entity 변환할 집중 세션 JPA 엔티티
     * @return 변환된 도메인 모델
     */
    FocusSession toModel(FocusSessionJpaEntity entity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     * modifiedAt은 자동으로 생성되므로 무시됩니다.
     *
     * @param domain 변환할 도메인 모델
     * @return 변환된 JPA 엔티티
     */
    @Mapping(target = "modifiedAt", ignore = true)
    FocusSessionJpaEntity toEntity(FocusSession domain);

    /**
     * 선택적 JPA 엔티티를 선택적 도메인 모델로 변환합니다.
     *
     * @param optionalEntity 변환할 선택적 JPA 엔티티
     * @return 변환된 선택적 도메인 모델
     */
    default Optional<FocusSession> toOptionalModel(Optional<FocusSessionJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    /**
     * JPA 엔티티 페이지를 도메인 모델 페이지로 변환합니다.
     *
     * @param entityPage 변환할 JPA 엔티티 페이지
     * @return 변환된 도메인 모델 페이지
     */
    default Page<FocusSession> toModelPage(Page<FocusSessionJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
