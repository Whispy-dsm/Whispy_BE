package whispy_server.whispy.domain.notification.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.DeleteNotificationPort;
import whispy_server.whispy.domain.notification.batch.dto.DeleteOldNotificationJobParameters;
import whispy_server.whispy.global.exception.domain.batch.BatchJobExecutionFailedException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteOldNotificationItemWriter implements ItemWriter<DeleteOldNotificationJobParameters> {

    private final DeleteNotificationPort deleteNotificationPort;

    @Override
    public void write(Chunk<? extends DeleteOldNotificationJobParameters> chunk) {
        try {
            List<Long> ids = chunk.getItems().stream()
                    .map(DeleteOldNotificationJobParameters::id)
                    .toList();

            deleteNotificationPort.deleteAllByIdInBatch(ids);
        } catch (Exception e) {
            throw BatchJobExecutionFailedException.EXCEPTION;
        }
    }
}
