package whispy_server.whispy.domain.announcement.adapter.in.web.dto.response;

public record QueryAnnouncementResponse(
        String title,
        String content,
        String bannerImageUrl
) {}
