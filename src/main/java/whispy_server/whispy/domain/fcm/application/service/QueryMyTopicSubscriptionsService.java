package whispy_server.whispy.domain.fcm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.fcm.adapter.in.web.dto.response.TopicSubscriptionResponse;
import whispy_server.whispy.domain.fcm.application.port.in.QueryMyTopicSubscriptionsUseCase;
import whispy_server.whispy.domain.fcm.application.port.out.QueryTopicSubscriptionPort;
import whispy_server.whispy.domain.fcm.model.TopicSubscription;
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
        List<TopicSubscription> subscriptions = queryTopicSubscriptionPort.findByEmail(
                userFacadeUseCase.currentUser().email()
        );

        return subscriptions.stream()
                .map(TopicSubscriptionResponse::from)
                .toList();
    }
}
