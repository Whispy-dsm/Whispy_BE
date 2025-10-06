package whispy_server.whispy.domain.history.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import whispy_server.whispy.domain.history.adapter.out.entity.ListeningHistoryJpaEntity;
import whispy_server.whispy.domain.history.model.ListeningHistory;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListeningHistoryMapper {

    ListeningHistoryJpaEntity toEntity(ListeningHistory listeningHistory);
    ListeningHistory toModel(ListeningHistoryJpaEntity listeningHistoryJpaEntity);

    default Optional<ListeningHistory> toOptionalModel(Optional<ListeningHistoryJpaEntity> optionalEntity) {
        return optionalEntity.map(this::toModel);
    }

    default Page<ListeningHistory> toModelPage(Page<ListeningHistoryJpaEntity> entityPage) {
        return entityPage.map(this::toModel);
    }
}
