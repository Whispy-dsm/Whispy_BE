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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMyTopicSubscriptionsService implements QueryMyTopicSubscriptionsUseCase {

    private final QueryTopicSubscriptionPort queryTopicSubscriptionPort;
    private final UserFacadeUseCase userFacadeUseCase;

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
