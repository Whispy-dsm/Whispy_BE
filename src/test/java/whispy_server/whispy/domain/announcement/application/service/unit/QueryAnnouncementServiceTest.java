package whispy_server.whispy.domain.announcement.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.application.service.QueryAnnouncementService;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * QueryAnnouncementService의 단위 테스트 클래스
 *
 * 공지사항 조회 서비스의 다양한 시나리오를 검증합니다.
 * 공지사항 상세 조회 및 예외 처리 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryAnnouncementService 테스트")
class QueryAnnouncementServiceTest {

    @InjectMocks
    private QueryAnnouncementService queryAnnouncementService;

    @Mock
    private AnnouncementPort announcementPort;

    @Test
    @DisplayName("존재하는 공지사항을 조회할 수 있다")
    void whenAnnouncementExists_thenReturnsDetails() {
        // given
        Long announcementId = 1L;
        Announcement announcement = createAnnouncement(
                announcementId,
                "새로운 기능 출시",
                "Whispy에 새로운 기능이 추가되었습니다.",
                "https://example.com/banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(announcement));

        // when
        QueryAnnouncementResponse response = queryAnnouncementService.execute(announcementId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("새로운 기능 출시");
        assertThat(response.content()).isEqualTo("Whispy에 새로운 기능이 추가되었습니다.");
        assertThat(response.bannerImageUrl()).isEqualTo("https://example.com/banner.jpg");
        verify(announcementPort).findById(announcementId);
    }

    @Test
    @DisplayName("존재하지 않는 공지사항을 조회하면 예외가 발생한다")
    void whenAnnouncementNotFound_thenThrowsException() {
        // given
        Long announcementId = 999L;
        given(announcementPort.findById(announcementId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> queryAnnouncementService.execute(announcementId))
                .isInstanceOf(AnnouncementNotFoundException.class);
        verify(announcementPort).findById(announcementId);
    }

    @Test
    @DisplayName("배너 이미지가 없는 공지사항을 조회할 수 있다")
    void whenNoBannerImage_thenReturnsDetailsWithNullBanner() {
        // given
        Long announcementId = 1L;
        Announcement announcement = createAnnouncement(
                announcementId,
                "간단한 공지",
                "간단한 공지사항 내용입니다.",
                null
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(announcement));

        // when
        QueryAnnouncementResponse response = queryAnnouncementService.execute(announcementId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("간단한 공지");
        assertThat(response.content()).isEqualTo("간단한 공지사항 내용입니다.");
        assertThat(response.bannerImageUrl()).isNull();
    }

    @Test
    @DisplayName("긴 내용의 공지사항을 조회할 수 있다")
    void whenLongContent_thenReturnsFullContent() {
        // given
        Long announcementId = 1L;
        String longContent = "이것은 매우 긴 공지사항 내용입니다. ".repeat(50);
        Announcement announcement = createAnnouncement(
                announcementId,
                "상세 공지사항",
                longContent,
                "https://example.com/banner.jpg"
        );

        given(announcementPort.findById(announcementId)).willReturn(Optional.of(announcement));

        // when
        QueryAnnouncementResponse response = queryAnnouncementService.execute(announcementId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo(longContent);
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
