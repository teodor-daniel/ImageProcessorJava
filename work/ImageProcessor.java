package work;

import java.awt.image.BufferedImage;

public abstract class ImageProcessor {
    public abstract BufferedImage process(BufferedImage image, int factor);

    public BufferedImage processMultiple(BufferedImage image, int... factors) { // multiple vargs.
        BufferedImage currentImage = image;
        for (int factor : factors) {
            currentImage = process(currentImage, factor);
        }
        return currentImage;
    }
}