package ru.alarh.downloader.configuration.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Specified web client for sending GET with body request.
 *
 * @author inkarnadin
 */
public class WebClient {

    private final RestTemplate restTemplate;

    public WebClient() {
        restTemplate = new RestTemplate(new ExtendHttpComponentsClientHttpRequestFactory());
    }

    /**
     * Do POST request.
     *
     * @param url domain address
     * @param requestBody body of request
     * @param clazz type of response
     * @return response
     */
    public ResponseEntity<?> doPost(String url, Object requestBody, Class<?> clazz) {
        return restTemplate.postForEntity(url, requestBody, clazz);
    }

    /**
     * Do GET with body request.
     *
     * @param url domain address
     * @param requestBody body of request
     * @param clazz type of response
     * @return response
     */
    public ResponseEntity<?> doGetWithBody(String url, Object requestBody, Class<?> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, (HttpEntity<?>) requestBody, clazz);
    }

}