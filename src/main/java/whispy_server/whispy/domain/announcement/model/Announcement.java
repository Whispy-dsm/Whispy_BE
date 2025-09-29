package whispy_server.whispy.domain.announcement.model;

import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record Announcement(
        Long id,
        String title,
        String content,
        String bannerImageUrl
) {}
