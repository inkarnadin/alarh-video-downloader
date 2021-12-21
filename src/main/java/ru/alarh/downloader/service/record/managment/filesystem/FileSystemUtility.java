package ru.alarh.downloader.service.record.managment.filesystem;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * File system utility class.
 *
 * @author inkarnadin
 */
@UtilityClass
public final class FileSystemUtility {

    private static final String ROOT = "result";

    /**
     * Create root directory.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createDirectoryIfAbsent() {
        new File(ROOT).mkdir();
    }

    /**
     * Save file to root directory.
     *
     * @param fileName name of file
     * @param data data as byte array
     */
    @SneakyThrows
    public static void writeDataToFile(String fileName, byte[] data) {
        Files.write(
                Paths.get(String.format("%s/%s.mp4", ROOT, fileName)),
                Objects.requireNonNull(data)
        );
    }

}