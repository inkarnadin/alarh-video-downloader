package ru.alarh.downloader.service.record.managment.header;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.alarh.downloader.domain.Target;

import java.util.Base64;

/**
 * Headers builder for search and download cameras records.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
public class RecordSearchAndDownloadHeadersBuilder implements HeaderBuilder {

    /**
     * Build headers map for request of search and downloading cameras archive.
     *
     * @param target remote host
     * @return headers
     */
    @Override
    public MultiValueMap<String, String> build(Target target) {
        String authString = Base64.getEncoder().encodeToString(String.format("%s:%s", target.getLogin(), target.getPassword()).getBytes());
        log.debug(authString);

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s", authString));
        headers.add(HttpHeaders.CONNECTION, "keep-alive");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);

        return headers;
    }

}