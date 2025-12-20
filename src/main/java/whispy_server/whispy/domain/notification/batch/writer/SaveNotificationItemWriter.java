package whispy_server.whispy.domain.notification.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.SaveNotificationPort;
import whispy_server.whispy.domain.notification.batch.dto.NotificationJobParameters;
import whispy_server.whispy.domain.notification.model.Notification;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.List;

/**
 * 알림 저장 Item Writer.
 *
 * NotificationJobParameters를 받아 Notification 도메인 모델로 변환하고 배치로 저장하는 배치 라이터입니다.
 */
@Component
@RequiredArgsConstructor
public class SaveNotificationItemWriter implements ItemWriter<NotificationJobParameters> {

    private final SaveNotificationPort saveNotificationPort;

    /**
     * 청크 단위로 알림을 배치 저장합니다.
     *
     * @param chunk 저장할 알림 파라미터 청크
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    @Override
    public void write(Chunk<? extends NotificationJobParameters> chunk) {
        try {
            List<Notification> notifications = chunk.getItems().stream()
                    .map(item -> new Notification(
                            null,
                            item.email(),
                            item.title(),
                            item.body(),
                            item.topic(),
                            item.data(),
                            false,
                            null
                    )).toList();

            saveNotificationPort.saveAll(notifications);

        } catch (Exception e) {
            throw new BatchJobExecutionFailedException(e);
        }
    }

}
