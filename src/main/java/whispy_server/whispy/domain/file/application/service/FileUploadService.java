package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.in.FileUploadUseCase;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.file.FileProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final FileProperties fileProperties;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder) {
        FileValidator.validateFile(file, imageFolder);

        String fileName = generateFileName(file.getOriginalFilename());
        String folder = imageFolder.toString().toLowerCase();
        String filePath = Paths.get(fileProperties.uploadPath(), folder, fileName).toString();

        try{
            Files.createDirectories(Paths.get(fileProperties.uploadPath(), folder));

            file.transferTo(new File(filePath));

            String fileUrl = generateFileUrl(folder, fileName);
            return new FileUploadResponse(fileUrl);

        }catch (IOException e){
            throw FileUploadFailedException.EXCEPTION;
        }
    }

    private String generateFileName(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return UUID.randomUUID().toString() + extension;
    }

    private String generateFileUrl(String folder, String fileName) {
        return fileProperties.baseUrl() + "/file/" + folder + "/" + fileName;
    }

}
