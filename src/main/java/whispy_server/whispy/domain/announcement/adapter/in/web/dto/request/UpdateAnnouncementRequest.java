package whispy_server.whispy.domain.announcement.adapter.in.web.dto.request;

public record UpdateAnnouncementRequest(
        Long id,
        String title,
        String content,
        String bannerImageUrl
) {
}
