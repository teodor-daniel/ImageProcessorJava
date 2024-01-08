import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner for user input

        System.out.println("Enter the file path of the image:");
        //String filePath = scanner.nextLine();
        String filePath = "image.bmp";
        String outputFilePath = "processed_image.bmp";

        try {
            BufferedImage image = new ImageReader(filePath).readImage(); // Read the image

            if (image != null) {
                System.out.println("Choose an option:");
                System.out.println("1. Zoom In");
                System.out.println("2. Zoom Out");

                int choice = scanner.nextInt();

                ImageProcessor processor = null;

                if (choice == 1) {
                    processor = new ZoomIn();
                    System.out.println("You chose to Zoom In.");
                } else if (choice == 2) {
                    processor = new ZoomOut();
                    System.out.println("You chose to Zoom Out.");
                }
                else {
                    System.out.println("Invalid choice. Exiting...");
                    return;
                }

                if (processor != null) {
                    System.out.println("Do you want to apply:");
                    System.out.println("1. Single operation");
                    System.out.println("2. Multiple operations");

                    int operationChoice = scanner.nextInt();

                    BufferedImage processedImage = null;

                    if (operationChoice == 1) {

                        System.out.println("Enter zoom factor:");
                        int factor = scanner.nextInt(); // Read zoom factor
                        processedImage = processor.process(image, factor);
                    }
                    else if (operationChoice == 2) {
                        System.out.println("Enter multiple factors separated by space (eg: 1 2 3):");
                        scanner.nextLine(); //remove the leftover newline
                        String[] factorsStr = scanner.nextLine().split(" ");
                        int[] factors = new int[factorsStr.length];

                        for (int i = 0; i < factorsStr.length; i++) {
                            factors[i] = Integer.parseInt(factorsStr[i]);
                        }

                        processedImage = processor.processMultiple(image, factors);
                    }
                    else {
                        System.out.println("Invalid choice. Exiting...");
                        return;
                    }

                    //If the processing worked save the image;
                    if (processedImage != null) {
                        boolean result = ImageIO.write(processedImage, "bmp", new File(outputFilePath));
                        if (result) {
                            System.out.println("Processed image saved successfully to " + outputFilePath);
                        } else {
                            System.out.println("Failed to save the processed image.");
                        }
                    }
                }
            } else {
                System.out.println("Failed to read the source image.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close(); // Close the scanner
        }
    }
}
