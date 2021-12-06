package ru.alarh.downloader.service.record.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Simple search result object.
 *
 * @author inkarnadin
 */
@Data
@RequiredArgsConstructor
public class SearchResultObject {

    private final String name;
    private final String host;
    private final Integer count;

}