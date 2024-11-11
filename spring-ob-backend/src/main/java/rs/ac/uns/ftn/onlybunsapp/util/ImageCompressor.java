package rs.ac.uns.ftn.onlybunsapp.util;
import net.coobird.thumbnailator.Thumbnails;
import java.io.IOException;

public class ImageCompressor {
    public static void compressImage(String sourceImagePath, String outputImagePath, float quality) throws IOException {
        Thumbnails.of(sourceImagePath)
                .scale(1)
                .outputQuality(quality)
                .toFile(outputImagePath);
    }
    public static void upscaleImage(String sourceImagePath, String outputImagePath, int width, int height) throws IOException {
        Thumbnails.of(sourceImagePath)
                .size(width, height)
                .keepAspectRatio(true)
                .toFile(outputImagePath);
    }
}
