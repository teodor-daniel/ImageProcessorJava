package work;

import java.awt.image.BufferedImage;

public class ImageProducer implements Runnable {
    private SharedImageResource resource;
    private String filePath;

    public ImageProducer(SharedImageResource resource, String filePath) {
        this.resource = resource;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            BufferedImage image = new ImageReader(filePath).readImage();
            resource.setImage(image);
            System.out.println("Image read by Producer and stored in shared resource.");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    }
}
