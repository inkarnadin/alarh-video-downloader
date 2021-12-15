package ru.alarh.downloader.service.record.managment;

import ru.alarh.downloader.domain.Target;

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
    Integer searchContent(Target target);

    /**
     * Download camera record.
     *
     * @param target host
     * @param playbackUri path to video file
     */
    void downloadContent(Target target, String playbackUri);

}
