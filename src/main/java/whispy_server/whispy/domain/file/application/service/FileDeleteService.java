package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import whispy_server.whispy.domain.file.application.port.in.FileDeleteUseCase;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileDeleteFailedException;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;
import whispy_server.whispy.global.file.FileProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileDeleteService implements FileDeleteUseCase {

    private final FileProperties fileProperties;

    @Override
    public void deleteFile(ImageFolder imageFolder, String fileName){

        try {
            boolean deleted = Files.deleteIfExists(Paths.get(fileProperties.uploadPath(), imageFolder.toString().toLowerCase(), fileName));
            if (!deleted) {
                throw FileNotFoundException.EXCEPTION;
            }
        } catch (IOException e) {
            throw FileDeleteFailedException.EXCEPTION;
        }
    }
}
