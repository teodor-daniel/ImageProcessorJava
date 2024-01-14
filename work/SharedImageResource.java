package work;
import java.awt.image.BufferedImage;

public class SharedImageResource {
    private BufferedImage image;
    private boolean isAvailable = false;
//The shared resource utilizes synchronization methods (wait and notify) to ensure proper coordination between the Producer and Consumer threads.
    public synchronized void setImage(BufferedImage img) throws InterruptedException {
        while (isAvailable) {
            wait();
        }
        this.image = img;
        isAvailable = true;
        notify();
    }

    public synchronized BufferedImage getImage() throws InterruptedException {
        while (!isAvailable) {
            wait();
        }
        isAvailable = false;
        notify();
        return image;
    }
}