package whispy_server.whispy.domain.announcement.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.CreateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.out.AnnouncementPort;
import whispy_server.whispy.domain.announcement.model.Announcement;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;

import java.time.LocalDateTime;

/**
 * 공지사항 생성 서비스.
 *
 * 새로운 공지사항을 생성하고 모든 사용자에게 FCM 푸시 알림을 전송하는 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class CreateAnnouncementService implements CreateAnnouncementUseCase {

    private final AnnouncementPort announcementPort;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;

    /**
     * 공지사항을 생성하고 푸시 알림을 전송합니다.
     *
     * @param request 생성할 공지사항 정보가 포함된 요청
     */
    @Transactional
    @Override
    public void execute(CreateAnnouncementRequest request) {
        Announcement announcement = new Announcement(
                null,
                request.title(),
                request.content(),
                request.bannerImageUrl(),
                LocalDateTime.now()
        );

        announcementPort.save(announcement);

        sendAnnouncementNotification();
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
