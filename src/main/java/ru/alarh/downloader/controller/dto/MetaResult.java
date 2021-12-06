package ru.alarh.downloader.controller.dto;

import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
public class MetaResult<T> {

    private int countAll;
    private int countSuccess;
    private Duration duration;

    private List<T> results;

}