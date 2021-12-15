package ru.alarh.downloader.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.alarh.downloader.configuration.web.WebClient;

/**
 * Web beans configuration class.
 *
 * @author inkarnadin
 */
@Configuration
public class WebConfiguration {

    /**
     * Web client bean configuration method.
     *
     * @return RestTemplate instance
     */
    @Bean
    WebClient webClient() {
        return new WebClient();
    }

}