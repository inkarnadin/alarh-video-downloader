package ru.alarh.downloader.service.record.managment;

import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;

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
    SearchResultObject searchContent(Target target);

    /**
     * Download camera record.
     *
     * @param target host
     * @param playback path to video file
     */
    void downloadContent(Target target, PlaybackObject playback);

}
