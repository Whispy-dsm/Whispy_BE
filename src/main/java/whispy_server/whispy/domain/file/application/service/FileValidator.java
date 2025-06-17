package whispy_server.whispy.domain.file.application.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class FileValidator {

    private static final Set<String> VALID_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".heic","heif", ".svg", ".webp", ".gif"
    );

    // 허용할 MIME 타입
    private static final Set<String> VALID_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/heic",
            "image/svg+xml", "image/webp", "image/gif"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;


    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다");
        }
        validateFileExtension(file);

        validateMimeType(file);

        validateFileSize(file);
    }


    private void validateFileExtension(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null || !originalFileName.contains(".")){
            throw new IllegalArgumentException("<UNK> <UNK> <UNK> <UNK> <UNK>.");
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if(!VALID_EXTENSIONS.contains(extension)){
            throw new IllegalArgumentException("dd");
        }
    }

    private void validateMimeType(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType == null || !VALID_MIME_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("지원하지 않는 파일 타입입니다: " + contentType);
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. 최대 5MB까지 가능합니다");
        }
    }
}
