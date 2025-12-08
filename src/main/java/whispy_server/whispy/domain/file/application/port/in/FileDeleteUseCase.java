package whispy_server.whispy.domain.file.application.port.in;

import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 저장된 파일을 삭제하는 유스케이스 계약이다.
 */
@UseCase
public interface FileDeleteUseCase {
    /**
     * 폴더와 파일명을 기반으로 실제 파일을 삭제한다.
     *
     * @param imageFolder 파일이 위치한 폴더
     * @param fileName    삭제할 파일명
     */
    void deleteFile(ImageFolder imageFolder, String fileName);
}
