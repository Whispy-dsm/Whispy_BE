package whispy_server.whispy.domain.file.application.port.in;

import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface FileDeleteUseCase {
    void deleteFile(ImageFolder imageFolder, String fileName);
}
