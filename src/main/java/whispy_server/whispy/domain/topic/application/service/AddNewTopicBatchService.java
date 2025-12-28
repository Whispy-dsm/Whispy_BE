package whispy_server.whispy.domain.topic.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;
import whispy_server.whispy.domain.topic.batch.trigger.AddNewTopicBatchTrigger;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 새로운 토픽 추가 서비스.
 *
 * 모든 사용자에게 새로운 토픽을 추가하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class AddNewTopicBatchService implements AddNewTopicForAllUsersUseCase {

    private final AddNewTopicBatchTrigger addNewTopicBatchTrigger;

    /**
     * 모든 사용자에게 새로운 토픽을 추가합니다.
     *
     * @param newTopic 새로운 토픽
     * @param defaultSubscribed 기본 구독 여부
     */
    @UserAction("새로운 토픽 추가")
    @Override
    public void execute(NotificationTopic newTopic, boolean defaultSubscribed) {
        addNewTopicBatchTrigger.trigger(newTopic, defaultSubscribed);
    }
}
