package whispy_server.whispy.domain.file.application.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.exception.domain.file.ImageInvalidException;
import whispy_server.whispy.global.exception.domain.file.WebPConverterNotFoundException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * 이미지 파일을 압축하고 WebP로 변환하는 유틸 컴포넌트.
 */
@Component
public class ImageCompressionConverter {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private static final float WEBP_QUALITY = 0.8f;

    /**
     * 입력 이미지 파일을 리사이즈·압축하여 WebP 스트림으로 반환한다.
     *
     * @param file 업로드된 이미지 파일
     * @return WebP InputStream
     */
    public InputStream compressImage(MultipartFile file) {
        try {
            BufferedImage processedImage = Thumbnails.of(file.getInputStream())
                    .scale(1.0) // 원본 크기 그대로 유지 (리사이즈 안 함)
                    .useExifOrientation(true)
                    .asBufferedImage();

            if (processedImage == null) {
                throw ImageInvalidException.EXCEPTION;
            }

            return convertToWebP(processedImage);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("No suitable ImageReader")) {
                throw ImageInvalidException.EXCEPTION;
            }
            throw new FileUploadFailedException(e);
        }
    }

    /**
     * BufferedImage를 WebP 포맷으로 변환한다.
     */
    private InputStream convertToWebP(BufferedImage image) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");

        if (!writers.hasNext()) {
            throw WebPConverterNotFoundException.EXCEPTION;
        }

        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();

        if (writeParam.canWriteCompressed()) {
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType("Lossy");
            writeParam.setCompressionQuality(WEBP_QUALITY);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try(ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } finally {
            writer.dispose();
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
