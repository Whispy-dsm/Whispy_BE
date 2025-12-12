package whispy_server.whispy.domain.file.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.in.FileUploadUseCase;
import whispy_server.whispy.domain.file.application.utils.FileValidator;
import whispy_server.whispy.domain.file.application.utils.ImageCompressionConverter;
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

/**
 * 파일 업로드 UseCase 구현체.
 *
 * 이미지/음원 파일 검증, 저장, URL 생성을 담당한다.
 */
@Service
@RequiredArgsConstructor
public class FileUploadService implements FileUploadUseCase {

    private final FileProperties fileProperties;
    private final ImageCompressionConverter imageCompressionConverter;

    /**
     * 파일을 업로드하고 접근 URL을 반환한다.
     *
     * @param file        업로드할 파일
     * @param imageFolder 저장 대상 폴더
     * @return 업로드 결과 응답
     */
    @Override
    public FileUploadResponse uploadFile(MultipartFile file, ImageFolder imageFolder) {
        FileValidator.validateFile(file, imageFolder);

        String fileName = generateFileName(file, imageFolder);
        String folder = imageFolder.toString().toLowerCase();

        try {
            Files.createDirectories(Paths.get(fileProperties.uploadPath(), folder));

            if (imageFolder == ImageFolder.MUSIC_FOLDER || imageFolder == ImageFolder.MUSIC_VIDEO) {
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

    /**
     * 이미지를 압축하여 저장한다.
     */
    private void uploadCompressedImage(MultipartFile file, String folder, String fileName) throws IOException {
        InputStream compressedImage = imageCompressionConverter.compressImage(file);
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        Files.copy(compressedImage, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 원본 파일을 그대로 저장한다.
     */
    private void uploadOriginalFile(MultipartFile file, String folder, String fileName) throws IOException {
        Path filePath = Paths.get(fileProperties.uploadPath(), folder, fileName);
        file.transferTo(filePath.toFile());
    }

    /**
     * 저장용 파일명을 생성한다.
     */
    private String generateFileName(MultipartFile file, ImageFolder imageFolder) {
        String originalFileName = file.getOriginalFilename();

        if (imageFolder == ImageFolder.MUSIC_FOLDER || imageFolder == ImageFolder.MUSIC_VIDEO) {
            return generateFileNameWithExtension(originalFileName);
        }

        return UUID.randomUUID().toString() + ".webp";
    }

    /**
     * 원본 확장자를 유지한 파일명을 생성한다.
     */
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

    /**
     * 접근 가능한 파일 URL을 만든다.
     */
    private String generateFileUrl(String folder, String fileName) {
        return fileProperties.baseUrl() + "/file/" + folder + "/" + fileName;
    }
}
