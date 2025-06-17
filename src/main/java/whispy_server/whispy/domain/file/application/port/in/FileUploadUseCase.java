package whispy_server.whispy.domain.file.application.port.in;

import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface FileUploadUseCase {

    FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder);
}
