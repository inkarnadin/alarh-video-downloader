package ru.alarh.downloader.service.source;

import ru.alarh.downloader.domain.Target;

import java.util.List;

/**
 * Abstract source preparation service.
 *
 * @author inkarnadin
 */
public interface SourcePrepareService {

    /**
     * Read target data from file which was specified via properties.
     *
     * @return target list
     */
    List<Target> readTargetFromFileSystem();

}
