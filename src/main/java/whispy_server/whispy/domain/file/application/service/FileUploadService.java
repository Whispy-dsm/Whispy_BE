package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.in.FileUploadUseCase;
import whispy_server.whispy.domain.file.application.util.FileValidator;
import whispy_server.whispy.domain.file.application.util.ImageCompressionConverter;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.file.FileProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final FileProperties fileProperties;
    private final ImageCompressionConverter imageCompressionConverter;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder) {
        FileValidator.validateFile(file, imageFolder);

        String fileName = generateFileName(file, imageFolder);
        String folder = imageFolder.toString().toLowerCase();

        try {
            Files.createDirectories(Paths.get(fileProperties.uploadPath(), folder));

            if (imageFolder == ImageFolder.MUSIC_FOLDER) {
                uploadOriginalFile(file, folder, fileName);
            } else {
                uploadCompressedImage(file, folder, fileName);
            }

            String fileUrl = generateFileUrl(folder, fileName);
            return new FileUploadResponse(fileUrl);

        } catch (IOException e) {
            throw FileUploadFailedException.EXCEPTION;
        }
    }

    private void uploadCompressedImage(MultipartFile file, String folder, String fileName) throws IOException {
        InputStream compressedImage = imageCompressionConverter.compressImage(file);
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        Files.copy(compressedImage, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void uploadOriginalFile(MultipartFile file, String folder, String fileName) throws IOException {
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        file.transferTo(filePath.toFile());
    }

    private String generateFileName(MultipartFile file, ImageFolder imageFolder) {
        String originalFileName = file.getOriginalFilename();

        if (imageFolder == ImageFolder.MUSIC_FOLDER) {
            return generateFileNameWithExtension(originalFileName);
        }

        return UUID.randomUUID().toString() + ".webp";
    }

    private String generateFileNameWithExtension(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }

        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex).toLowerCase();
        }

        return UUID.randomUUID().toString() + extension;
    }

    private String generateFileUrl(String folder, String fileName) {
        return fileProperties.baseUrl() + "/file/" + folder + "/" + fileName;
    }
}