package ru.alarh.downloader.service.source;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.properties.SourceProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Source preparation service.
 *
 * @author inkarnadin
 */
@Service
@RequiredArgsConstructor
public class SourcePrepareServiceImpl implements SourcePrepareService {

    private final SourceProperties properties;

    /**
     * Read target data from file which was specified via {@link ru.alarh.downloader.properties.SourceProperties}.
     * If target string not contains at least four colon symbols, it will be skipped.
     *
     * @return target list
     */
    @Override
    @SneakyThrows
    public List<Target> readTargetFromFileSystem() {
        Path path = Path.of(properties.getPath());
        return Files.readAllLines(path).stream()
                .filter(x -> x.chars().filter(ch -> ch == ':').count() >= 4)
                .map(x -> {
                    String[] targets = x.split(":");
                    return new Target(targets[0], targets[1], targets[2], targets[3], targets[4]);
                })
                .collect(Collectors.toList());
    }

}