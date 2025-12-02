package whispy_server.whispy.domain.notification.batch.dto;

/**
 * 오래된 알림 삭제 배치 작업 파라미터.
 *
 * 오래된 알림 삭제 배치 작업에서 사용되는 파라미터 정보입니다.
 *
 * @param id 삭제할 알림 ID
 */
public record DeleteOldNotificationJobParameters(
        Long id
) {
}
