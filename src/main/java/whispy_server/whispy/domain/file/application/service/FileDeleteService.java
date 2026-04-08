package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.file.application.port.in.FileDeleteUseCase;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.annotation.UserAction;

/**
 * 파일 삭제 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final FileStoragePort fileStoragePort;

    /**
     * 지정된 폴더/파일명을 기반으로 파일을 삭제한다.
     *
     * @param imageFolder 폴더 타입
     * @param fileName    삭제할 파일명
     */
    @Override
    @UserAction("파일 삭제")
    public void deleteFile(ImageFolder imageFolder, String fileName){
        fileStoragePort.delete(ImageFolderPathResolver.toObjectKey(imageFolder, fileName));
    }
}
