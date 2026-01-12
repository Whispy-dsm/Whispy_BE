package whispy_server.whispy.domain.announcement.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.service.component.AnnouncementDeleter;
import whispy_server.whispy.domain.announcement.application.service.component.AnnouncementSaver;
import whispy_server.whispy.domain.announcement.application.service.CreateAnnouncementService;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * CreateAnnouncementService의 단위 테스트 클래스
 *
 * 공지사항 생성 서비스의 다양한 시나리오를 검증합니다.
 * 공지사항 생성 및 푸시 알림 전송 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAnnouncementService 테스트")
class CreateAnnouncementServiceTest {

    @InjectMocks
    private CreateAnnouncementService createAnnouncementService;

    @Mock
    private AnnouncementSaver announcementSaver;

    @Mock
    private AnnouncementDeleter announcementDeleter;

    @Mock
    private BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;

    @Test
    @DisplayName("유효한 공지사항을 생성할 수 있다")
    void whenValidAnnouncement_thenCreatesSuccessfully() {
        // given
        CreateAnnouncementRequest request = new CreateAnnouncementRequest(
                "새로운 기능 출시",
                "Whispy에 새로운 기능이 추가되었습니다."
        );

        // when
        createAnnouncementService.execute(request);

        // then
        verify(announcementSaver).save(any());
    }

    @Test
    @DisplayName("공지사항 생성 시 푸시 알림을 전송한다")
    void whenCreatingAnnouncement_thenSendsNotification() {
        // given
        CreateAnnouncementRequest request = new CreateAnnouncementRequest(
                "서비스 점검 안내",
                "내일 새벽 1시부터 3시까지 서비스 점검이 있습니다."
        );

        // when
        createAnnouncementService.execute(request);

        // then
        verify(broadCastToAllUsersUseCase).execute(any(NotificationTopicSendRequest.class));
    }

    @Test
    @DisplayName("긴 내용의 공지사항을 생성할 수 있다")
    void whenLongContent_thenCreatesSuccessfully() {
        // given
        String longContent = "이것은 매우 긴 공지사항 내용입니다. ".repeat(50);
        CreateAnnouncementRequest request = new CreateAnnouncementRequest(
                "상세 공지사항",
                longContent
        );

        // when
        createAnnouncementService.execute(request);

        // then
        verify(announcementSaver).save(any());
        verify(broadCastToAllUsersUseCase).execute(any(NotificationTopicSendRequest.class));
    }
}
