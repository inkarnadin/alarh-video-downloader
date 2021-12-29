package ru.alarh.downloader.service.record.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Download result transfer object.
 */
@Data
@RequiredArgsConstructor
public class DownloadResultObject {

    private final String videoFileName;

}