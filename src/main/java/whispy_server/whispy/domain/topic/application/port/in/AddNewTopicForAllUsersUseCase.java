package whispy_server.whispy.domain.topic.application.port.in;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 모든 사용자에게 새로운 토픽 추가 유스케이스.
 *
 * 배치 작업을 통해 모든 사용자에게 새로운 토픽을 추가합니다.
 */
@UseCase
public interface AddNewTopicForAllUsersUseCase {
    /**
     * 모든 사용자에게 새로운 토픽을 추가합니다.
     *
     * @param newTopic 새로운 토픽
     * @param defaultSubscribed 기본 구독 여부
     */
    void execute(NotificationTopic newTopic, boolean defaultSubscribed);
}
