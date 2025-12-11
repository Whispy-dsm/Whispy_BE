package whispy_server.whispy.domain.topic.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import whispy_server.whispy.domain.topic.application.service.AddNewTopicBatchService;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * AddNewTopicBatchService의 단위 테스트 클래스
 * <p>
 * 새로운 토픽 배치 추가 서비스를 검증합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddNewTopicBatchService 테스트")
class AddNewTopicBatchServiceTest {

    @InjectMocks
    private AddNewTopicBatchService service;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job addNewTopicJob;

    @Test
    @DisplayName("새로운 토픽을 배치로 추가할 수 있다")
    void execute() throws Exception {
        service.execute(NotificationTopic.GENERAL_ANNOUNCEMENT, true);

        verify(jobLauncher).run(any(Job.class), any());
    }
}
