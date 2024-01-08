import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageReader {
    private String filePath;

    public ImageReader(String filePath) {
        this.filePath = filePath;
    }

    public BufferedImage readImage() {
        try {
            File file = new File(this.filePath);
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}