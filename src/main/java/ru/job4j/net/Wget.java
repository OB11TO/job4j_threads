package ru.job4j.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private static final String FILE_PATH = "tmp.xml";

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        long startAt = System.currentTimeMillis();
        File file = new File(FILE_PATH);
        try (InputStream inputStream = new URL(url).openStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                long downloadAt = System.nanoTime();
                outputStream.write(buffer, 0, readBytes);
                long endDownload = System.nanoTime() - downloadAt;
                System.out.println("Read " + " " + readBytes + " bytes : " + endDownload + " nano.");
                long actualSpeed = (long) ((readBytes * 1_000_000.0) / endDownload);
                checkSpeedDownload(actualSpeed);
            }
            System.out.println("\nFile size is " + Files.size(file.toPath()) + " bytes");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkSpeedDownload(long actualSpeed) throws InterruptedException {
        if (actualSpeed >= speed) {
            System.out.println("Limit the download speed!");
            System.out.println("Actual speed: " + actualSpeed + " more then acceptable " + speed);
            long sleepMillis = actualSpeed / speed;
            System.out.println("Sleep: " + sleepMillis + " ms");
            Thread.sleep(sleepMillis);
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("The number of arguments is incorrect." + System.lineSeparator()
                    + "First argument – URL" + System.lineSeparator()
                    + "Second – speed (in bytes);" + System.lineSeparator());
        }
        String url = args[0];
        String speedStr = args[1];

        if (!isValidURL(url)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        try {
            int speed = Integer.parseInt(speedStr);
            if (speed <= 0) {
                throw new IllegalArgumentException("Speed must be a positive integer.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Speed must be a valid integer.");
        }
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validateArgs(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}