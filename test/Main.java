package test;

import work.*;
import work.ImageReader; // Renamed to avoid conflict with javax.imageio.ImageReader

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path of the image:");
        //String filePath = scanner.nextLine();
        String filePath = "image.bmp";
        String outputFilePath = "processed_image.bmp";

        File sourceFile = new File(filePath);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            System.out.println("Source file does not exist or is not a file: " + filePath);
            return;
        }
        try {
            SharedImageResource sharedResource = new SharedImageResource();

            // Start Producer thread to read the image, is done by the ImageReader class.
            //Once the image is read, it's stored in a shared resource (SharedImageResource), and the thread prints the time taken to load the image.
            Thread producerThread = new Thread(() -> {
                try {
                    long loadImageTimer = System.currentTimeMillis();
                    BufferedImage image = new ImageReader(filePath).readImage();
                    sharedResource.setImage(image);
                    System.out.println("Image read by Producer and stored in shared resource.");
                    Thread.sleep(5000); // Sleep to simulate delay and show thread communication
                    long elapsedTimeMillis = System.currentTimeMillis() - loadImageTimer;
                    System.out.println("Time to load the image: " + elapsedTimeMillis / 1000F + " seconds" + '\n');
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            producerThread.start();

            int choice = 0;
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Zoom In");
                System.out.println("2. Zoom Out");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    if (choice == 1 || choice == 2) {
                        break;
                    } else {
                        System.out.println("Please enter 1 or 2.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            }

            ImageProcessor processor = (choice == 1) ? new ZoomIn() : new ZoomOut(); //efectiv trebuie ceva de genu if(choice == 1) zoom in else zoom out
            System.out.println("Do you want to apply:");
            System.out.println("1. Single operation");
            System.out.println("2. Multiple operations");

            int operationChoice = scanner.nextInt();
            int[] factors;

            if (operationChoice == 2) {
                System.out.println("Enter multiple factors separated by space (eg: 1 2 3):");
                scanner.nextLine(); // Consume the leftover newline
                String[] factorsStr = scanner.nextLine().split(" ");
                factors = new int[factorsStr.length];
                for (int i = 0; i < factorsStr.length; i++) {
                    factors[i] = Integer.parseInt(factorsStr[i]);
                }
            } else {
                System.out.println("Enter factor size: ");
                factors = new int[]{scanner.nextInt()};
            }

            // Start Consumer thread to process the image
            Thread consumerThread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    long processImageTimer = System.currentTimeMillis();
                    BufferedImage image = sharedResource.getImage();
                    BufferedImage processedImage = null;
                    if (operationChoice == 1 || operationChoice == 2) {
                        processedImage = processor.processMultiple(image, factors);
                    }

                    if (processedImage != null) {
                        boolean result = ImageIO.write(processedImage, "bmp", new File(outputFilePath));
                        if (result) {
                            System.out.println("Processed image saved successfully to " + outputFilePath);
                            long elapsedTimeMillis = System.currentTimeMillis() - processImageTimer;
                            System.out.println("Time to complete processing: " + elapsedTimeMillis / 1000F + " seconds");

                        } else {
                            System.out.println("Failed to save the processed image.");
                        }
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });

            consumerThread.start();

            //The main thread(static void main) waits for both of these processes to terminate, so it can close.
            producerThread.join();
            consumerThread.join();

        } catch ( InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
