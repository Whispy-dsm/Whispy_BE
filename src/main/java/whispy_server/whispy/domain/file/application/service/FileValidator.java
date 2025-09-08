package whispy_server.whispy.domain.file.application.service;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.type.ImageFolder;
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

    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    public void validateFile(MultipartFile file, ImageFolder folder) {
        validateFileName(file);

        switch (folder) {
            case PROFILE_IMAGE_FOLDER, MUSIC_BANNER_IMAGE_FOLDER -> validateImageFile(file);
            case MUSIC_FOLDER -> validateMusicFile(file);
        }
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

    private void validateImageFile(MultipartFile file) {
        validateImageExtension(file);
        validateImageMimeType(file);
        validateImageSize(file);
    }

    private void validateMusicFile(MultipartFile file) {
        validateMusicExtension(file);
        validateMusicMimeType(file);
        validateMusicSize(file);
    }

    private void validateImageExtension(MultipartFile file) {
        String extension = getFileExtension(file);
        Set<String> validExtensions = Set.of(".jpg", ".jpeg", ".png", ".heic", ".heif", ".webp", ".gif");

        if (!validExtensions.contains(extension)) {
            throw FileInvalidExtensionException.EXCEPTION;
        }
    }

    private void validateMusicExtension(MultipartFile file) {
        String extension = getFileExtension(file);
        Set<String> validExtensions = Set.of(".mp3", ".wav", ".flac", ".aac", ".ogg", ".m4a");

        if (!validExtensions.contains(extension)) {
            throw FileInvalidExtensionException.EXCEPTION;
        }
    }
    private void validateImageMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        Set<String> validMimeTypes = Set.of(
                "image/jpeg", "image/png", "image/heic", "image/webp", "image/gif", "image/heif"
        );

        if (contentType == null || !validMimeTypes.contains(contentType)) {
            throw FileInvalidMimeTypeException.EXCEPTION;
        }
    }

    private void validateMusicMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        Set<String> validMimeTypes = Set.of(
                "audio/mpeg", "audio/wav", "audio/flac",
                "audio/aac", "audio/ogg", "audio/mp4"
        );

        if (contentType == null || !validMimeTypes.contains(contentType)) {
            throw FileInvalidMimeTypeException.EXCEPTION;
        }
    }

    private void validateImageSize(MultipartFile file) {
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw FileSizeExceededException.EXCEPTION;
        }
    }

    private void validateMusicSize(MultipartFile file) {
        long maxSize = 50 * 1024 * 1024; // 50MB
        if (file.getSize() > maxSize) {
            throw FileSizeExceededException.EXCEPTION;
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw FileNoExtensionException.EXCEPTION;
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
    }
}
