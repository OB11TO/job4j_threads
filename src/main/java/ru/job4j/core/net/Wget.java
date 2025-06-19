package ru.job4j.core.net;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

@Slf4j
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
        log.info("Run method started for URL={}", url);
        long startAt = System.currentTimeMillis();
        File file = new File(FILE_PATH);
        try (InputStream inputStream = new URL(url).openStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            log.info("Open connection established in {} ms", System.currentTimeMillis() - startAt);
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                long downloadAt = System.nanoTime();
                outputStream.write(buffer, 0, readBytes);
                long endDownload = System.nanoTime() - downloadAt;
                log.info("Read {} bytes in {} ns", readBytes, endDownload);
                long actualSpeed = (long) ((readBytes * 1_000_000.0) / endDownload);
                checkSpeedDownload(actualSpeed);
            }
            long totalTime = System.currentTimeMillis() - startAt;
            log.info("File size is {} bytes", Files.size(file.toPath()));
            log.info("Download completed in {} ms", totalTime);
        } catch (InterruptedException e) {
            log.warn("Download thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            log.error("I/O exception occurred during download", e);
            throw new RuntimeException(e);
        }
    }

    private void checkSpeedDownload(long actualSpeed) throws InterruptedException {
        if (actualSpeed >= speed) {
            log.info("Speed {} bytes/s is above limit {} bytes/s; throttling download", actualSpeed, speed);
            long sleepMillis = actualSpeed / speed;
            log.info("Sleeping for {} ms to limit speed", sleepMillis);
            Thread.sleep(sleepMillis);
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length != 2) {
            log.error("Incorrect number of arguments: {}", args.length);
            throw new IllegalArgumentException("The number of arguments is incorrect." + System.lineSeparator()
                    + "First argument – URL" + System.lineSeparator()
                    + "Second – speed (in bytes);" + System.lineSeparator());
        }
        String url = args[0];
        String speedStr = args[1];

        if (!isValidURL(url)) {
            log.error("Invalid URL provided: {}", url);
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        try {
            int speed = Integer.parseInt(speedStr);
            if (speed <= 0) {
                log.error("Non-positive speed provided: {}", speed);
                throw new IllegalArgumentException("Speed must be a positive integer.");
            }
            log.info("Arguments validated successfully: URL={} speed={}", url, speed);
        } catch (NumberFormatException e) {
            log.error("Speed is not a valid integer: {}", speedStr);
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
        log.info("Starting download for URL={} with speed={}", url, speed);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
        log.info("Download thread completed for URL={}", url);
    }
}