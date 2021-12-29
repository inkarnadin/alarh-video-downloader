package ru.alarh.downloader.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Search properties.
 *
 * @author inkarnadin
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alarh.search")
public class SearchProperties {

    private String startDate;
    private String endDate;
    private int resultLimit;
    private int startPosition;

}