package ru.alarh.downloader.controller.dto;

import lombok.Data;

import java.time.Duration;
import java.util.List;

/**
 * Metadata information class.
 * Includes count of attempt, success attempts, operation duration and list of result by specified type.
 *
 * @param <T> type of result list
 *
 * @author inkarnadin
 */
@Data
public class MetaResult<T> {

    private int countAll;
    private int countSuccess;
    private Duration duration;

    private List<T> results;

}