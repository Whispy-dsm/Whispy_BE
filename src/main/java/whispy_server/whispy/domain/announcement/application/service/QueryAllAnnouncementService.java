package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.in.QueryAllAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryAllAnnouncementService implements QueryAllAnnouncementUseCase {

    private final AnnouncementPort announcementPort;

    @Transactional(readOnly = true)
    @Override
    public Page<QueryAllAnnouncementResponse> execute(Pageable pageable) {
        return announcementPort.findAllByOrderByCreatedAtDesc(pageable)
                .map(announcement -> new QueryAllAnnouncementResponse(
                        announcement.id(),
                        announcement.title(),
                        announcement.content(),
                        announcement.bannerImageUrl()
                ));
    }
}