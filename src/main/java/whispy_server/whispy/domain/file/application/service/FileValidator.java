package whispy_server.whispy.domain.file.application.service;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.global.exception.domain.file.FileInvalidExtensionException;
import whispy_server.whispy.global.exception.domain.file.FileInvalidMimeTypeException;
import whispy_server.whispy.global.exception.domain.file.FileNameContainsPathException;
import whispy_server.whispy.global.exception.domain.file.FileNameEmptyException;
import whispy_server.whispy.global.exception.domain.file.FileNameInvalidCharException;
import whispy_server.whispy.global.exception.domain.file.FileNameTooLongException;
import whispy_server.whispy.global.exception.domain.file.FileNoExtensionException;
import whispy_server.whispy.global.exception.domain.file.FileSizeExceededException;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class FileValidator {

    private static final Set<String> VALID_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".heic",".heif", ".svg", ".webp", ".gif"
    );

    // 허용할 MIME 타입
    private static final Set<String> VALID_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/heic",
            "image/svg+xml", "image/webp", "image/gif", "image/heif"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");



    public void validateFile(MultipartFile file) {

        validateFileName(file);

        validateFileExtension(file);

        validateMimeType(file);

        validateFileSize(file);


    }


    private void validateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if(originalFileName == null || originalFileName.trim().isEmpty()){
            throw FileNameEmptyException.EXCEPTION;
        }

        if (originalFileName.contains("..") || originalFileName.contains("/") || originalFileName.contains("\\")) {
            throw FileNameContainsPathException.EXCEPTION;
        }

        if (!SAFE_FILENAME_PATTERN.matcher(originalFileName).matches()) {
            throw FileNameInvalidCharException.EXCEPTION;
        }

        if (originalFileName.length() > 255) {
            throw FileNameTooLongException.EXCEPTION;
        }
    }

    private void validateFileExtension(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        if(!originalFileName.contains(".")){
            throw FileNoExtensionException.EXCEPTION;
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if(!VALID_EXTENSIONS.contains(extension)){
            throw FileInvalidExtensionException.EXCEPTION;
        }
    }

    private void validateMimeType(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType == null || !VALID_MIME_TYPES.contains(contentType)) {
            throw FileInvalidMimeTypeException.EXCEPTION;
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw FileSizeExceededException.EXCEPTION;
        }
    }
}
