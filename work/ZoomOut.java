package work;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ZoomOut extends ImageProcessor {
    private int factor;

    static {
        System.out.println("work.ZoomOut class loaded!");
    }

    {
        System.out.println("New work.ZoomOut instance created!");
    }

    public ZoomOut() {
        this.factor = factor;
    }

    @Override
    public BufferedImage process(BufferedImage image, int factor) {
        if (image == null) {
            throw new IllegalArgumentException("Source image cannot be null");
        }

        if (factor <= 0) {
            throw new IllegalArgumentException("Zoom factor must be greater than zero");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        //Dimension for the reduced image
        int reducedWidth = width / factor;
        int reducedHeight = height / factor;

        // Create a new image with the smaller size
        BufferedImage reducedImage = new BufferedImage(reducedWidth, reducedHeight, image.getType());

        // Perform the averaging (zoom out)
        for (int y = 0; y < reducedHeight; y++) {
            for (int x = 0; x < reducedWidth; x++) {
                // as the color is not maintained we average the color, and based on it's neighbours we change accordingly.
                int pixelColor = averageColor(image, x * factor, y * factor, factor);
                reducedImage.setRGB(x, y, pixelColor);
            }
        }

        // Return the reduced image
        return reducedImage;
    }


    //https://stackoverflow.com/questions/28162488/get-average-color-on-bufferedimage-and-bufferedimage-portion-as-fast-as-possible
    private  int averageColor(BufferedImage img, int startX, int startY, int factor) {
        long sumR = 0;
        long sumG = 0;
        long sumB = 0;
        int count = 0;

        for (int y = startY; y < startY + factor && y < img.getHeight(); y++) {
            for (int x = startX; x < startX + factor && x < img.getWidth(); x++) {
                Color pixel = new Color(img.getRGB(x, y));
                sumR += pixel.getRed();
                sumG += pixel.getGreen();
                sumB += pixel.getBlue();
                count++;
            }
        }

        if (count == 0) return 0; // if there are no pixels in the set interval return nothing.

        int avgR = (int) (sumR / count);
        int avgG = (int) (sumG / count);
        int avgB = (int) (sumB / count);

        return (avgR << 16) | (avgG << 8) | avgB; // 24 bits, red green blue in order
    }
}