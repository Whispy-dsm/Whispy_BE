package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.CreateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.service.component.AnnouncementDeleter;
import whispy_server.whispy.domain.announcement.application.service.component.AnnouncementSaver;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.global.exception.domain.announcement.AnnouncementPublicationFailedException;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDateTime;

/**
 * 공지사항 생성 서비스.
 *
 * 새로운 공지사항을 생성하고 모든 사용자에게 FCM 푸시 알림을 전송하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class CreateAnnouncementService implements CreateAnnouncementUseCase {

    private final AnnouncementSaver announcementSaver;
    private final AnnouncementDeleter announcementDeleter;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;

    /**
     * 공지사항을 생성하고 푸시 알림을 전송합니다.
     *
     * @param request 생성할 공지사항 정보가 포함된 요청
     */
    @Override
    @UserAction("공지사항 생성")
    public void execute(CreateAnnouncementRequest request) {
        // Announcement 객체 생성 (서비스의 책임)
        Announcement announcement = new Announcement(
                null,
                request.title(),
                request.content(),
                request.bannerImageUrl(),
                LocalDateTime.now()
        );

        Long savedId = announcementSaver.save(announcement);

        // 트랜잭션 밖에서 알림 전송
        try {
            sendAnnouncementNotification();
        } catch (Exception e) {
            // 알림 전송 실패 시 저장된 공지사항 롤백
            announcementDeleter.delete(savedId);
            throw new AnnouncementPublicationFailedException(e);
        }
    }

    /**
     * 모든 사용자에게 공지사항 등록 알림을 전송합니다.
     */
    private void sendAnnouncementNotification() {
        NotificationTopicSendRequest notificationRequest = new NotificationTopicSendRequest(
                null,
                "공지사항",
                "새로운 공지사항이 등록되었습니다.",
                null
        );

        broadCastToAllUsersUseCase.execute(notificationRequest);
    }
}
