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

@Component
public class ImageCompressionConverter {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 1024;
    private static final float WEBP_QUALITY = 0.8f;

    public InputStream compressImage(MultipartFile file) {
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            if(originalImage == null) {
                throw ImageInvalidException.EXCEPTION;
            }

            BufferedImage resizedImage = resizeImage(originalImage);

            return convertToWebP(resizedImage);
        } catch (IOException e) {
            throw FileUploadFailedException.EXCEPTION;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return originalImage;
        }

        return Thumbnails.of(originalImage)
                .size(MAX_WIDTH, MAX_HEIGHT)
                .asBufferedImage();
    }

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
