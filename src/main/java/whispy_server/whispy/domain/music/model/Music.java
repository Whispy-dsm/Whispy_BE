package whispy_server.whispy.domain.music.model;

import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record Music(
        Long id,
        String title,
        String filePath,
        int duration,
        MusicCategory category,
        String bannerImageUrl
) {

    public Music update(
            String newTitle,
            String newFilePath,
            Integer newDuration,
            MusicCategory newCategory,
            String newBannerImageUrl
    ) {
        return new Music(
                this.id,
                newTitle != null ? newTitle : this.title,
                newFilePath != null ? newFilePath : this.filePath,
                newDuration != null ? newDuration : this.duration,
                newCategory != null ? newCategory : this.category,
                newBannerImageUrl != null ? newBannerImageUrl : this.bannerImageUrl
        );
    }
}