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
     * Create directory for downloading video.
     *
     * @param directoryName name of directory
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    synchronized public static void createDirectoryIfAbsent(String directoryName) {
        new File(ROOT).mkdir();
        new File(String.format("%s/%s", ROOT, directoryName)).mkdir();
    }

    /**
     * Save file to root directory.
     *
     * @param directoryName name of directory
     * @param fileName name of file
     * @param data data as byte array
     */
    @SneakyThrows
    public static void writeDataToFile(String directoryName, String fileName, byte[] data) {
        Files.write(
                Paths.get(String.format("%s/%s.mp4", String.format("%s/%s", ROOT, directoryName), fileName)),
                Objects.requireNonNull(data)
        );
    }

}