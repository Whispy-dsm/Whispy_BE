package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

public record QueryAllAnnouncementResponse(
        Long id,
        String title,
        String content,
        String bannerImageUrl
) {
}
