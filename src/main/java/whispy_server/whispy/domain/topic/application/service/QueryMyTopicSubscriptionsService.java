package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.topic.application.port.in.QueryMyTopicSubscriptionsUseCase;
import whispy_server.whispy.domain.topic.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.topic.model.TopicSubscription;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;

import java.util.List;

/**
 * 내 토픽 구독 목록 조회 서비스.
 *
 * 현재 사용자의 토픽 구독 목록을 조회하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMyTopicSubscriptionsService implements QueryMyTopicSubscriptionsUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 내 토픽 구독 목록을 조회합니다.
     *
     * @return 토픽 구독 목록
     */
    @Override
    public List<TopicSubscriptionResponse> execute(){
        String currentUserEmail = userFacadeUseCase.currentUser().email();
        List<TopicSubscription> subscriptions = queryTopicSubscriptionPort.findByEmail(currentUserEmail);

        return subscriptions.stream()
                .filter(topics ->
                                topics.topic() == NotificationTopic.GENERAL_ANNOUNCEMENT
                        )
                .map(TopicSubscriptionResponse::from)
                .toList();
    }
}
