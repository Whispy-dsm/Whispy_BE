package whispy_server.whispy.domain.file.application.utils;

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

/**
 * 파일 확장자, MIME, 크기, 이름 등을 검증하는 유틸리티.
 */
public final class FileValidator {

    private static final int MAX_FILENAME_LENGTH = 255;

    private static final long IMAGE_MAX_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    private static final long MUSIC_MAX_SIZE_BYTES = 100 * 1024 * 1024; // 100MB
    private static final long VIDEO_MAX_SIZE_BYTES = 100 * 1024 * 1024; // 100MB

    private static final Set<String> IMAGE_VALID_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".heic", ".heif", ".webp", ".gif");
    private static final Set<String> MUSIC_VALID_EXTENSIONS = Set.of(".mp3", ".wav", ".flac", ".aac", ".ogg", ".m4a");
    private static final Set<String> VIDEO_VALID_EXTENSIONS = Set.of(".mp4", ".mov", ".avi", ".mkv", ".webm");

    private static final Set<String> IMAGE_VALID_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/heic", "image/webp", "image/gif", "image/heif"
    );
    private static final Set<String> MUSIC_VALID_MIME_TYPES = Set.of(
            "audio/mpeg", "audio/wav","audio/x-wav", "audio/wave", "audio/vnd.wave", "audio/flac", "audio/aac", "audio/ogg", "audio/mp4"
    );
    private static final Set<String> VIDEO_VALID_MIME_TYPES = Set.of(
            "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/webm"
    );

    private FileValidator() {
        throw new AssertionError("유틸리티 클래스는 객체를 생성하지 않도록 설계해야 합니다.");
    }

    /**
     * 파일을 폴더 타입에 맞춰 검증한다.
     *
     * @param file   업로드 파일
     * @param folder 대상 폴더
     */
    public static void validateFile(MultipartFile file, ImageFolder folder) {
        validateFileName(file);

        switch (folder) {
            case PROFILE_IMAGE_FOLDER, MUSIC_BANNER_IMAGE_FOLDER -> validateImageFile(file);
            case MUSIC_FOLDER -> validateMusicFile(file);
            case MUSIC_VIDEO_FOLDER -> validateVideoFile(file);
        }
    }

    private static void validateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if(originalFileName == null || originalFileName.trim().isEmpty()){
            throw FileNameEmptyException.EXCEPTION;
        }

        if (originalFileName.contains("..") || originalFileName.contains("/") || originalFileName.contains("\\")) {
            throw FileNameContainsPathException.EXCEPTION;
        }

        if (originalFileName.length() > MAX_FILENAME_LENGTH) {
            throw FileNameTooLongException.EXCEPTION;
        }
    }

    private static void validateImageFile(MultipartFile file) {
        validateFileType(file, IMAGE_VALID_EXTENSIONS, IMAGE_VALID_MIME_TYPES, IMAGE_MAX_SIZE_BYTES);
    }

    private static void validateMusicFile(MultipartFile file) {
        validateFileType(file, MUSIC_VALID_EXTENSIONS, MUSIC_VALID_MIME_TYPES, MUSIC_MAX_SIZE_BYTES);
    }

    private static void validateVideoFile(MultipartFile file) {
        validateFileType(file, VIDEO_VALID_EXTENSIONS, VIDEO_VALID_MIME_TYPES, VIDEO_MAX_SIZE_BYTES);
    }

    /**
     * 파일 타입을 검증하는 공통 메서드.
     *
     * @param file            업로드 파일
     * @param validExtensions 유효한 확장자 목록
     * @param validMimeTypes  유효한 MIME 타입 목록
     * @param maxSizeBytes    최대 파일 크기(바이트)
     */
    private static void validateFileType(MultipartFile file, Set<String> validExtensions,
                                         Set<String> validMimeTypes, long maxSizeBytes) {
        validateExtension(file, validExtensions);
        validateMimeType(file, validMimeTypes);
        validateSize(file, maxSizeBytes);
    }

    /**
     * 파일 확장자를 검증한다.
     *
     * @param file            업로드 파일
     * @param validExtensions 유효한 확장자 목록
     */
    private static void validateExtension(MultipartFile file, Set<String> validExtensions) {
        String extension = getFileExtension(file);

        if (!validExtensions.contains(extension)) {
            throw FileInvalidExtensionException.EXCEPTION;
        }
    }

    /**
     * 파일 MIME 타입을 검증한다.
     *
     * @param file           업로드 파일
     * @param validMimeTypes 유효한 MIME 타입 목록
     */
    private static void validateMimeType(MultipartFile file, Set<String> validMimeTypes) {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw FileInvalidMimeTypeException.EXCEPTION;
        }

        // MIME 타입에서 세미콜론 이후의 파라미터 제거 (예: "image/heic; charset=..." -> "image/heic")
        String baseMimeType = contentType.split(";")[0].trim();

        if (!validMimeTypes.contains(baseMimeType)) {
            throw FileInvalidMimeTypeException.EXCEPTION;
        }
    }

    /**
     * 파일 크기를 검증한다.
     *
     * @param file         업로드 파일
     * @param maxSizeBytes 최대 파일 크기(바이트)
     */
    private static void validateSize(MultipartFile file, long maxSizeBytes) {
        if (file.getSize() > maxSizeBytes) {
            throw FileSizeExceededException.EXCEPTION;
        }
    }

    private static String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw FileNoExtensionException.EXCEPTION;
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
    }
}
