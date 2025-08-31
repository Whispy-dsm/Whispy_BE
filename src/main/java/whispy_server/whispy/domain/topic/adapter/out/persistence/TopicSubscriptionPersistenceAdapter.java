package whispy_server.whispy.domain.topic.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.topic.adapter.out.entity.TopicSubscriptionJpaEntity;
import whispy_server.whispy.domain.topic.adapter.out.mapper.TopicSubscriptionEntityMapper;
import whispy_server.whispy.domain.topic.adapter.out.persistence.repository.TopicSubscriptionRepository;
import whispy_server.whispy.domain.topic.application.port.out.TopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TopicSubscriptionPersistenceAdapter implements TopicSubscriptionPort {

    private final TopicSubscriptionRepository topicSubscriptionRepository;
    private final TopicSubscriptionEntityMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TopicSubscription> findByEmail(String email){
        List<TopicSubscriptionJpaEntity> entities =  topicSubscriptionRepository.findByEmail(email);
        return mapper.toModelList(entities);
    }

    @Override
    public Optional<TopicSubscription> findByEmailAndTopic(String email, NotificationTopic topic){
        Optional<TopicSubscriptionJpaEntity> optionalEntity = topicSubscriptionRepository.findByEmailAndTopic(email, topic);
        return mapper.toOptionalModel(optionalEntity);
    }

    @Override
    public List<TopicSubscription> findSubscribedUserByTopic(NotificationTopic topic){
        List<TopicSubscriptionJpaEntity> entities = topicSubscriptionRepository.findByTopicAndIsSubscribedTrue(topic);
        return mapper.toModelList(entities);
    }

    @Override
    public TopicSubscription save(TopicSubscription subscription) {
        TopicSubscriptionJpaEntity entity = mapper.toEntity(subscription);
        TopicSubscriptionJpaEntity savedEntity = topicSubscriptionRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public List<TopicSubscription> saveAll(List<TopicSubscription> subscriptions) {
        List<TopicSubscriptionJpaEntity> entities = mapper.toEntityList(subscriptions);
        List<TopicSubscriptionJpaEntity> savedEntities = topicSubscriptionRepository.saveAll(entities);
        return mapper.toModelList(savedEntities);
    }
}
