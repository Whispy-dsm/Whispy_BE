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

/**
 * 토픽 구독 영속성 어댑터.
 *
 * 토픽 구독 정보의 영속성을 관리하는 아웃바운드 어댑터입니다.
 */
@Component
@RequiredArgsConstructor
public class TopicSubscriptionPersistenceAdapter implements TopicSubscriptionPort {

    private final TopicSubscriptionRepository topicSubscriptionRepository;
    private final TopicSubscriptionEntityMapper mapper;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 이메일로 토픽 구독 목록을 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 토픽 구독 목록
     */
    @Override
    public List<TopicSubscription> findByEmail(String email){
        List<TopicSubscriptionJpaEntity> entities =  topicSubscriptionRepository.findByEmail(email);
        return mapper.toModelList(entities);
    }

    /**
     * 이메일과 토픽으로 토픽 구독을 조회합니다.
     *
     * @param email 사용자 이메일
     * @param topic 알림 토픽
     * @return 토픽 구독 Optional
     */
    @Override
    public Optional<TopicSubscription> findByEmailAndTopic(String email, NotificationTopic topic){
        Optional<TopicSubscriptionJpaEntity> optionalEntity = topicSubscriptionRepository.findByEmailAndTopic(email, topic);
        return mapper.toOptionalModel(optionalEntity);
    }

    /**
     * 특정 토픽을 구독 중인 사용자 목록을 조회합니다.
     *
     * @param topic 알림 토픽
     * @return 구독 중인 사용자의 토픽 구독 목록
     */
    @Override
    public List<TopicSubscription> findSubscribedUserByTopic(NotificationTopic topic){
        List<TopicSubscriptionJpaEntity> entities = topicSubscriptionRepository.findByTopicAndSubscribedTrue(topic);
        return mapper.toModelList(entities);
    }

    /**
     * 토픽 구독을 저장합니다.
     *
     * @param subscription 토픽 구독
     * @return 저장된 토픽 구독
     */
    @Override
    public TopicSubscription save(TopicSubscription subscription) {
        TopicSubscriptionJpaEntity entity = mapper.toEntity(subscription);
        TopicSubscriptionJpaEntity savedEntity = topicSubscriptionRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    /**
     * 토픽 구독 목록을 일괄 저장합니다.
     *
     * @param subscriptions 토픽 구독 목록
     * @return 저장된 토픽 구독 목록
     */
    @Override
    public List<TopicSubscription> saveAll(List<TopicSubscription> subscriptions) {
        List<TopicSubscriptionJpaEntity> entities = mapper.toEntityList(subscriptions);
        List<TopicSubscriptionJpaEntity> savedEntities = topicSubscriptionRepository.saveAll(entities);
        return mapper.toModelList(savedEntities);
    }

    /**
     * 이메일로 토픽 구독을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    @Override
    public void deleteByEmail(String email) {
        topicSubscriptionRepository.deleteByEmail(email);
    }
}
