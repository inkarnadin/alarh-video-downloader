package ru.alarh.downloader.configuration.web;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;

public class WebClient {

    private final RestTemplate restTemplate;

    public WebClient() {
        restTemplate = new RestTemplate(new ExtendHttpComponentsClientHttpRequestFactory());
    }

    public ResponseEntity<?> doPost(String url, Object requestBody, Class<?> clazz) {
        return restTemplate.postForEntity(url, requestBody, clazz);
    }

    public ResponseEntity<?> doGetWithBody(String url, Object requestBody, Class<?> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, (HttpEntity<?>) requestBody, clazz);
    }

}