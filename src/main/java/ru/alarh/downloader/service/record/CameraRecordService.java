package ru.alarh.downloader.service.record;

import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.service.record.dto.SearchResultObject;

/**
 * Abstract camera record service.
 *
 * @author inkarnadin
 */
public interface CameraRecordService {

    /**
     * Aggregate method for search by all targets.
     *
     * @return metadata object with searching results
     */
    MetaResult<SearchResultObject> searchByAll();

    MetaResult<String> downloadRecord();

}
