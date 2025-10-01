package whispy_server.whispy.domain.announcement.adapter.in.web.dto.request;

public record CreateAnnouncementRequest(
        String title,
        String content,
        String bannerImageUrl
) {
}
