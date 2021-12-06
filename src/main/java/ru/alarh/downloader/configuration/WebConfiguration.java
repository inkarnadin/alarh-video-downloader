package ru.alarh.downloader.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Web beans configuration class.
 *
 * @author inkarnadin
 */
@Configuration
public class WebConfiguration {

    /**
     * RestTemplate bean configuration method.
     *
     * @return RestTemplate instance
     */
    @Bean
    RestTemplate httpClient() {
        return new RestTemplate();
    }

}