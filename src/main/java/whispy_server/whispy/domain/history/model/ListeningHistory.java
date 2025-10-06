package whispy_server.whispy.domain.history.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.time.LocalDateTime;

@Aggregate
public record ListeningHistory(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime listenedAt
) {
    public ListeningHistory updateListenedAt() {
        return new ListeningHistory(
                this.id,
                this.userId,
                this.musicId,
                LocalDateTime.now()
        );
    }
}
