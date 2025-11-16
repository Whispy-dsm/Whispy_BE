package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

import whispy_server.whispy.domain.announcement.model.Announcement;

public record QueryAllAnnouncementResponse(
        Long id,
        String title,
        String content,
        String bannerImageUrl
) {

    public static QueryAllAnnouncementResponse from(Announcement announcement) {
        return new QueryAllAnnouncementResponse(
                announcement.id(),
                announcement.title(),
                announcement.content(),
                announcement.bannerImageUrl()
        );
    }
}
