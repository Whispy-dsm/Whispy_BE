package whispy_server.whispy.domain.fcm.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.fcm.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.fcm.model.TopicSubscription;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicSubscriptionEntityMapper {

    TopicSubscription toModel(TopicSubscriptionJpaEntity topicSubscriptionJpaEntity);
    TopicSubscriptionJpaEntity toEntity(TopicSubscription topicSubscription);

    List<TopicSubscription> toModelList(List<TopicSubscriptionJpaEntity> topicSubscriptionJpaEntityList);
    List<TopicSubscriptionJpaEntity> toEntityList(List<TopicSubscription> subscriptions);

    default Optional<TopicSubscription> toOptionalModel(Optional<TopicSubscriptionJpaEntity> topicSubscriptionJpaEntity) {
        return topicSubscriptionJpaEntity.map(this::toModel);
    }

}
