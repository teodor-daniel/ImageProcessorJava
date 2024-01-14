package work;

import java.awt.image.BufferedImage;

public class ZoomIn extends ImageProcessor {
    private int factor;

    static {
        System.out.println("work.ZoomIn class loaded!");
    }

    {
        System.out.println("New work.ZoomIn instance created!" );
    }

    public ZoomIn() {
        this.factor = factor;
    }

    @Override
    public BufferedImage process(BufferedImage srcImage, int factor) {
        if (srcImage == null) {
            throw new IllegalArgumentException("Source image cannot be null");
        }

        if (factor <= 0) {
            throw new IllegalArgumentException("Zoom factor must be greater than zero");
        }

        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        //New dimensions based on the zoom factor.
        int zoomedWidth = width * factor;
        int zoomedHeight = height * factor;

        BufferedImage zoomedImage = new BufferedImage(zoomedWidth, zoomedHeight, srcImage.getType()); //image that is * factor size

        //https://www.javatpoint.com/dip-concept-of-sampling algorithm
        for (int y = 0; y < zoomedHeight; y++) {
            for (int x = 0; x < zoomedWidth; x++) {
                //find the corresponding pixel in the source image
                int srcX = x / factor;
                int srcY = y / factor;

                // Copy the pixel color from the source image
                int pixelColor = srcImage.getRGB(srcX, srcY);

                // Set the pixel color in the new zoomed image
                zoomedImage.setRGB(x, y, pixelColor);//the color is maintained.
            }
        }

        return zoomedImage;
    }

}