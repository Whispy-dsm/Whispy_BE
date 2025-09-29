package whispy_server.whispy.domain.music.adapter.in.web.dto.request;

import whispy_server.whispy.domain.music.model.type.MusicCategory;

public record UpdateMusicRequest(
        Long id,
        String title,
        String filePath, 
        Integer duration,
        MusicCategory category
) {}
