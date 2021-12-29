package ru.alarh.downloader.service.record.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Search result transfer object.
 *
 * @author inkarnadin
 */
@Data
@RequiredArgsConstructor
public class SearchResultObject {

    private final String name;
    private final String host;
    private final Integer count;
    private final List<String> playbacks;

}