package whispy_server.whispy.domain.history.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.history.adapter.out.entity.ListeningHistoryJpaEntity;
import whispy_server.whispy.domain.history.model.ListeningHistory;

import java.util.Optional;

/**
 * 청취 이력 엔터티-도메인 변환을 담당하는 MapStruct 매퍼.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListeningHistoryMapper {

    /**
     * 도메인 모델을 엔터티로 변환한다.
     */
    ListeningHistoryJpaEntity toEntity(ListeningHistory listeningHistory);

    /**
     * 엔터티를 도메인 모델로 변환한다.
     */
    ListeningHistory toModel(ListeningHistoryJpaEntity listeningHistoryJpaEntity);

    default Optional<ListeningHistory> toOptionalModel(Optional<ListeningHistoryJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    default Page<ListeningHistory> toModelPage(Page<ListeningHistoryJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
