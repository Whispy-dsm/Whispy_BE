package whispy_server.whispy.domain.announcement.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.application.service.UpdateAnnouncementService;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * UpdateAnnouncementService의 단위 테스트 클래스
 *
 * 공지사항 수정 서비스의 다양한 시나리오를 검증합니다.
 * 공지사항 수정 및 예외 처리 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateAnnouncementService 테스트")
class UpdateAnnouncementServiceTest {

    @InjectMocks
    private UpdateAnnouncementService updateAnnouncementService;

    @Mock
    private AnnouncementPort announcementPort;

    @Test
    @DisplayName("공지사항을 수정할 수 있다")
    void whenValidRequest_thenUpdatesSuccessfully() {
        // given
        Long announcementId = 1L;
        Announcement existingAnnouncement = createAnnouncement(
                announcementId,
                "기존 제목",
                "기존 내용",
                "https://example.com/old-banner.jpg"
        );

        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                "수정된 제목",
                "수정된 내용",
                "https://example.com/new-banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(existingAnnouncement));

        // when
        updateAnnouncementService.execute(request);

        // then
        verify(announcementPort).findById(announcementId);
        verify(announcementPort).save(any(Announcement.class));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항을 수정하면 예외가 발생한다")
    void whenAnnouncementNotFound_thenThrowsException() {
        // given
        Long announcementId = 999L;
        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                "수정된 제목",
                "수정된 내용",
                "https://example.com/new-banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> updateAnnouncementService.execute(request))
                .isInstanceOf(AnnouncementNotFoundException.class);
        verify(announcementPort).findById(announcementId);
    }

    @Test
    @DisplayName("제목만 수정할 수 있다")
    void whenUpdatingTitleOnly_thenUpdatesSuccessfully() {
        // given
        Long announcementId = 1L;
        Announcement existingAnnouncement = createAnnouncement(
                announcementId,
                "기존 제목",
                "기존 내용",
                "https://example.com/banner.jpg"
        );

        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                "새로운 제목",
                null,
                null
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(existingAnnouncement));

        // when
        updateAnnouncementService.execute(request);

        // then
        verify(announcementPort).save(any(Announcement.class));
    }

    @Test
    @DisplayName("내용만 수정할 수 있다")
    void whenUpdatingContentOnly_thenUpdatesSuccessfully() {
        // given
        Long announcementId = 1L;
        Announcement existingAnnouncement = createAnnouncement(
                announcementId,
                "기존 제목",
                "기존 내용",
                "https://example.com/banner.jpg"
        );

        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                null,
                "새로운 내용",
                null
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(existingAnnouncement));

        // when
        updateAnnouncementService.execute(request);

        // then
        verify(announcementPort).save(any(Announcement.class));
    }

    @Test
    @DisplayName("배너 이미지만 수정할 수 있다")
    void whenUpdatingBannerOnly_thenUpdatesSuccessfully() {
        // given
        Long announcementId = 1L;
        Announcement existingAnnouncement = createAnnouncement(
                announcementId,
                "기존 제목",
                "기존 내용",
                "https://example.com/old-banner.jpg"
        );

        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                null,
                null,
                "https://example.com/new-banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(existingAnnouncement));

        // when
        updateAnnouncementService.execute(request);

        // then
        verify(announcementPort).save(any(Announcement.class));
    }

    @Test
    @DisplayName("모든 필드를 한 번에 수정할 수 있다")
    void whenUpdatingAllFields_thenUpdatesSuccessfully() {
        // given
        Long announcementId = 1L;
        Announcement existingAnnouncement = createAnnouncement(
                announcementId,
                "기존 제목",
                "기존 내용",
                "https://example.com/old-banner.jpg"
        );

        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest(
                announcementId,
                "완전히 새로운 제목",
                "완전히 새로운 내용",
                "https://example.com/completely-new-banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(existingAnnouncement));

        // when
        updateAnnouncementService.execute(request);

        // then
        verify(announcementPort).findById(announcementId);
        verify(announcementPort).save(any(Announcement.class));
    }

    /**
     * 테스트용 Announcement 객체를 생성합니다.
     *
     * @param id 공지사항 ID
     * @param title 공지사항 제목
     * @param content 공지사항 내용
     * @param bannerImageUrl 배너 이미지 URL
     * @return 생성된 Announcement 객체
     */
    private Announcement createAnnouncement(Long id, String title, String content, String bannerImageUrl) {
        return new Announcement(
                id,
                title,
                content,
                bannerImageUrl,
                LocalDateTime.now()
        );
    }
}
