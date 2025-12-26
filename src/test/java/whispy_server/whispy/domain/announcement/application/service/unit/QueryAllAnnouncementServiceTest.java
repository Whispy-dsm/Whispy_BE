package whispy_server.whispy.domain.announcement.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.application.service.QueryAllAnnouncementService;
import whispy_server.whispy.domain.announcement.model.Announcement;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * QueryAllAnnouncementService의 단위 테스트 클래스
 *
 * 모든 공지사항 조회 서비스의 다양한 시나리오를 검증합니다.
 * 페이지네이션된 공지사항 목록 조회 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryAllAnnouncementService 테스트")
class QueryAllAnnouncementServiceTest {

    @InjectMocks
    private QueryAllAnnouncementService queryAllAnnouncementService;

    @Mock
    private AnnouncementPort announcementPort;

    @Test
    @DisplayName("모든 공지사항을 페이지네이션하여 조회할 수 있다")
    void whenQueryingAllAnnouncements_thenReturnsPagedResults() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Announcement> announcements = List.of(
                createAnnouncement(1L, "공지사항 1", "내용 1"),
                createAnnouncement(2L, "공지사항 2", "내용 2"),
                createAnnouncement(3L, "공지사항 3", "내용 3")
        );
        Page<Announcement> announcementPage = new PageImpl<>(announcements, pageable, announcements.size());

        given(announcementPort.findAllByOrderByCreatedAtDesc(pageable)).willReturn(announcementPage);

        // when
        Page<QueryAllAnnouncementResponse> result = queryAllAnnouncementService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        verify(announcementPort).findAllByOrderByCreatedAtDesc(pageable);
    }

    @Test
    @DisplayName("공지사항이 없을 때 빈 페이지를 반환한다")
    void whenNoAnnouncements_thenReturnsEmptyPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Announcement> emptyPage = Page.empty(pageable);

        given(announcementPort.findAllByOrderByCreatedAtDesc(pageable)).willReturn(emptyPage);

        // when
        Page<QueryAllAnnouncementResponse> result = queryAllAnnouncementService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("두 번째 페이지를 조회할 수 있다")
    void whenQueryingSecondPage_thenReturnsCorrectPage() {
        // given
        Pageable pageable = PageRequest.of(1, 5);
        List<Announcement> announcements = List.of(
                createAnnouncement(6L, "공지사항 6", "내용 6"),
                createAnnouncement(7L, "공지사항 7", "내용 7")
        );
        Page<Announcement> announcementPage = new PageImpl<>(announcements, pageable, 12);

        given(announcementPort.findAllByOrderByCreatedAtDesc(pageable)).willReturn(announcementPage);

        // when
        Page<QueryAllAnnouncementResponse> result = queryAllAnnouncementService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(12);
        assertThat(result.getNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("페이지 크기를 커스터마이징할 수 있다")
    void whenCustomPageSize_thenReturnsCorrectSize() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        List<Announcement> announcements = createAnnouncementList(20);
        Page<Announcement> announcementPage = new PageImpl<>(announcements, pageable, announcements.size());

        given(announcementPort.findAllByOrderByCreatedAtDesc(pageable)).willReturn(announcementPage);

        // when
        Page<QueryAllAnnouncementResponse> result = queryAllAnnouncementService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(20);
        assertThat(result.getSize()).isEqualTo(20);
    }

    /**
     * 테스트용 Announcement 객체를 생성합니다.
     *
     * @param id 공지사항 ID
     * @param title 공지사항 제목
     * @param content 공지사항 내용
     * @return 생성된 Announcement 객체
     */
    private Announcement createAnnouncement(Long id, String title, String content) {
        return new Announcement(
                id,
                title,
                content,
                "https://example.com/banner.jpg",
                LocalDateTime.now()
        );
    }

    /**
     * 테스트용 Announcement 리스트를 생성합니다.
     *
     * @param count 생성할 공지사항 개수
     * @return 생성된 Announcement 리스트
     */
    private List<Announcement> createAnnouncementList(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> createAnnouncement((long) i, "공지사항 " + i, "내용 " + i))
                .toList();
    }
}
