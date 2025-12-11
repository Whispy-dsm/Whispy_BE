package whispy_server.whispy.domain.announcement.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.application.service.DeleteAnnouncementService;

import static org.mockito.Mockito.verify;

/**
 * DeleteAnnouncementService의 단위 테스트 클래스
 * <p>
 * 공지사항 삭제 서비스의 다양한 시나리오를 검증합니다.
 * 공지사항 삭제 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAnnouncementService 테스트")
class DeleteAnnouncementServiceTest {

    @InjectMocks
    private DeleteAnnouncementService deleteAnnouncementService;

    @Mock
    private AnnouncementPort announcementPort;

    @Test
    @DisplayName("공지사항을 삭제할 수 있다")
    void whenValidId_thenDeletesSuccessfully() {
        // given
        Long announcementId = 1L;

        // when
        deleteAnnouncementService.execute(announcementId);

        // then
        verify(announcementPort).deleteById(announcementId);
    }

    @Test
    @DisplayName("다른 ID의 공지사항을 삭제할 수 있다")
    void whenDifferentId_thenDeletesSuccessfully() {
        // given
        Long announcementId = 999L;

        // when
        deleteAnnouncementService.execute(announcementId);

        // then
        verify(announcementPort).deleteById(announcementId);
    }
}
