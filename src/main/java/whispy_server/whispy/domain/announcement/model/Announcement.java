package whispy_server.whispy.domain.announcement.model;

import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record Announcement(
        Long id,
        String title,
        String content,
        String bannerImageUrl
) {
    public Announcement update(UpdateAnnouncementRequest request) {
        return new Announcement(
                this.id,
                request.title() != null ? request.title() : this.title,
                request.content() != null ? request.content() : this.content,
                request.bannerImageUrl() != null ? request.bannerImageUrl() : this.bannerImageUrl
        );
    }
}
