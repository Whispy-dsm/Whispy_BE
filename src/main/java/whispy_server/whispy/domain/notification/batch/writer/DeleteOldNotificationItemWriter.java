package whispy_server.whispy.domain.notification.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.batch.dto.DeleteOldNotificationJobParameters;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.List;

/**
 * 오래된 알림 삭제 Item Writer.
 *
 * DeleteOldNotificationJobParameters를 받아 배치로 알림을 삭제하는 배치 라이터입니다.
 */
@Component
@RequiredArgsConstructor
public class DeleteOldNotificationItemWriter implements ItemWriter<DeleteOldNotificationJobParameters> {

    private final DeleteNotificationPort deleteNotificationPort;

    /**
     * 청크 단위로 알림을 배치 삭제합니다.
     *
     * @param chunk 삭제할 알림 파라미터 청크
     * @throws BatchJobExecutionFailedException 배치 작업 실행 실패 시
     */
    @Override
    public void write(Chunk<? extends DeleteOldNotificationJobParameters> chunk) {
        try {
            List<Long> ids = chunk.getItems().stream()
                    .map(DeleteOldNotificationJobParameters::id)
                    .toList();

            deleteNotificationPort.deleteAllByIdInBatch(ids);
        } catch (Exception e) {
            throw new BatchJobExecutionFailedException(e);
        }
    }
}
