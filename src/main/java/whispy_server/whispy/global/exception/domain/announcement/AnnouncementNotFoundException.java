package whispy_server.whispy.global.exception.domain.announcement;

import whispy_server.whispy.global.exception.WhispyException;
import whispy_server.whispy.global.exception.error.ErrorCode;

public class AnnouncementNotFoundException extends WhispyException {

    public static final AnnouncementNotFoundException EXCEPTION = new AnnouncementNotFoundException();

    private AnnouncementNotFoundException() {
        super(ErrorCode.ANNOUNCEMENT_NOT_FOUND);
    }
}
