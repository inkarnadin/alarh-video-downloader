package ru.alarh.downloader.service.record;

import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.service.record.dto.DownloadResultObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;

/**
 * Abstract camera record service.
 *
 * @author inkarnadin
 */
public interface CameraRecordService {

    /**
     * Method for search by all targets.
     *
     * @return metadata object with searching results
     */
    MetaResult<SearchResultObject> searchByAll();

    /**
     * Method for download by all targets.
     *
     * @return metadata object with downloading results
     */
    MetaResult<DownloadResultObject> downloadRecords();

}
