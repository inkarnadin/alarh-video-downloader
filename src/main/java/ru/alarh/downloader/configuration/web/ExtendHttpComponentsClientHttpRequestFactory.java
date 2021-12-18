package ru.alarh.downloader.configuration.web;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * Extend web client request factory.
 *
 * @author inkarnadin
 */
public class ExtendHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    /**
     * Override create http uri request method.
     * If method equals GET, create custom Http Entity request class.
     *
     * @param httpMethod method
     * @param uri domain address
     * @return uri request
     */
    @Override
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
        if (HttpMethod.GET.equals(httpMethod))
            return new HttpEntityEnclosingGetRequestBase(uri);
        return super.createHttpUriRequest(httpMethod, uri);
    }

}