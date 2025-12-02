package whispy_server.whispy.global.exception.domain.announcement;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

/**
 * 공지사항을 찾을 수 없을 때 발생하는 예외.
 *
 * 요청한 ID에 해당하는 공지사항이 데이터베이스에 존재하지 않을 때 발생합니다.
 * 싱글톤 패턴으로 구현되어 있으며, EXCEPTION 상수를 통해 재사용됩니다.
 */
public class AnnouncementNotFoundException extends WhispyException {

    public static final AnnouncementNotFoundException EXCEPTION = new AnnouncementNotFoundException();

    private AnnouncementNotFoundException() {
        super(ErrorCode.ANNOUNCEMENT_NOT_FOUND);
    }
}
