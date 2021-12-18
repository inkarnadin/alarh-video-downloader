package ru.alarh.downloader.configuration.web;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.springframework.http.HttpMethod;

import java.net.URI;

/**
 * Custom http entity get request base class.
 *
 * @author inkarnadin
 */
public class HttpEntityEnclosingGetRequestBase extends HttpEntityEnclosingRequestBase {

    public HttpEntityEnclosingGetRequestBase(final URI uri) {
        super.setURI(uri);
    }

    /**
     * Override method name getter for GET http method.
     *
     * @return GET method name
     */
    @Override
    public String getMethod() {
        return HttpMethod.GET.name();
    }

}