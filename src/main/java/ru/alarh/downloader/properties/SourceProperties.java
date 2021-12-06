package ru.alarh.downloader.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Source properties.
 *
 * @author inkarnadin
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alarh.source")
public class SourceProperties {

    private String path;

}