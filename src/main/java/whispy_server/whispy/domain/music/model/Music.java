package whispy_server.whispy.domain.music.model;

import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record Music(
        Long id,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {

    public Music update(UpdateMusicRequest request) {
        return new Music(
                this.id,
                request.title() != null ? request.title() : this.title,
                request.filePath() != null ? request.filePath() : this.filePath,
                request.duration() != null ? request.duration() : this.duration,
                request.category() != null ? request.category() : this.category
        );
    }
}
