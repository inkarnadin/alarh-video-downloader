package ru.alarh.downloader.service.record.managment;

import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;

import java.util.Optional;

/**
 * Abstract content manager class for working with camera records data.
 *
 * @author inkarnadin
 */
public interface ContentManagementService {

    /**
     * Search camera records.
     *
     * @param target host
     * @return count of finding records
     */
    Optional<SearchResultObject> searchContent(Target target);

    /**
     * Download camera record.
     *
     * @param target host
     * @param playback path to video file
     */
    boolean downloadContent(Target target, PlaybackObject playback);

}
