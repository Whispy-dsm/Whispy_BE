package whispy_server.whispy.domain.topic.adapter.out.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.topic.model.TopicSubscription;

import java.util.List;
import java.util.Optional;

/**
 * 토픽 구독 엔티티 매퍼.
 *
 * JPA 엔티티와 도메인 모델 간의 변환을 담당합니다.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicSubscriptionEntityMapper {

    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     *
     * @param topicSubscriptionJpaEntity 토픽 구독 JPA 엔티티
     * @return 토픽 구독 도메인 모델
     */
    TopicSubscription toModel(TopicSubscriptionJpaEntity topicSubscriptionJpaEntity);

    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     *
     * @param topicSubscription 토픽 구독 도메인 모델
     * @return 토픽 구독 JPA 엔티티
     */
    TopicSubscriptionJpaEntity toEntity(TopicSubscription topicSubscription);

    /**
     * JPA 엔티티 리스트를 도메인 모델 리스트로 변환합니다.
     *
     * @param topicSubscriptionJpaEntityList 토픽 구독 JPA 엔티티 리스트
     * @return 토픽 구독 도메인 모델 리스트
     */
    List<TopicSubscription> toModelList(List<TopicSubscriptionJpaEntity> topicSubscriptionJpaEntityList);

    /**
     * 도메인 모델 리스트를 JPA 엔티티 리스트로 변환합니다.
     *
     * @param subscriptions 토픽 구독 도메인 모델 리스트
     * @return 토픽 구독 JPA 엔티티 리스트
     */
    List<TopicSubscriptionJpaEntity> toEntityList(List<TopicSubscription> subscriptions);

    /**
     * Optional JPA 엔티티를 Optional 도메인 모델로 변환합니다.
     *
     * @param topicSubscriptionJpaEntity 토픽 구독 JPA 엔티티 Optional
     * @return 토픽 구독 도메인 모델 Optional
     */
    default Optional<TopicSubscription> toOptionalModel(Optional<TopicSubscriptionJpaEntity> topicSubscriptionJpaEntity) {
        return topicSubscriptionJpaEntity.map(this::toModel);
    }

}
