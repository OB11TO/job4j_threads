package ru.job4j.core.visability;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Predicate;

@Slf4j
public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent() {
        return strategyConnect((character -> true));
    }

    public String getContentWithoutUnicode() {
        return strategyConnect(character -> character < 0x80);
    }

    private String strategyConnect(Predicate<Character> predicate) {
        StringBuilder output = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = bis.read()) != -1) {
                if (predicate.test((char) data)) {
                    output.append((char) data);
                }
            }
            return output.toString();
        } catch (FileNotFoundException e) {
            log.error("File: {} not found!", file, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}