package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.topic.application.service.AddNewTopicBatchService;
import whispy_server.whispy.domain.topic.batch.trigger.AddNewTopicBatchTrigger;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import static org.mockito.Mockito.verify;

/**
 * AddNewTopicBatchService의 단위 테스트 클래스
 *
 * 새로운 토픽 배치 추가 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddNewTopicBatchService 테스트")
class AddNewTopicBatchServiceTest {

    @InjectMocks
    private AddNewTopicBatchService service;

    @Mock
    private AddNewTopicBatchTrigger addNewTopicBatchTrigger;

    @Test
    @DisplayName("새로운 토픽을 배치로 추가할 수 있다")
    void execute() {
        // when
        service.execute(NotificationTopic.GENERAL_ANNOUNCEMENT, true);

        // then
        verify(addNewTopicBatchTrigger).trigger(NotificationTopic.GENERAL_ANNOUNCEMENT, true);
    }
}
