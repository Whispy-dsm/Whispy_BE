package whispy_server.whispy.global.exception.domain.announcement;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 공지사항 발행에 실패했을 때 발생하는 예외.
 *
 * 공지사항 저장 후 알림 전송(FCM 또는 배치 작업) 중 오류가 발생했을 때 사용됩니다.
 * 이 예외가 발생하면 저장된 공지사항은 자동으로 롤백됩니다.
 */
public class AnnouncementPublicationFailedException extends WhispyException {

    public AnnouncementPublicationFailedException(Throwable cause) {
        super(ErrorCode.ANNOUNCEMENT_PUBLICATION_FAILED, cause);
    }
}
