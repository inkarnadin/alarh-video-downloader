package ru.alarh.downloader.service.record.managment;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.exception.EmptyCredentialsException;
import ru.alarh.downloader.exception.EmptyResponseException;
import ru.alarh.downloader.service.record.managment.builder.RequestBuilder;
import ru.alarh.downloader.service.record.managment.template.search.SearchRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchResponseXML;
import ru.alarh.downloader.service.xml.MarshallingService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

/**
 * Content manager class for working with camera records data.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentManagementServiceISAPI implements ContentManagementService {

    private static final String SEARCH = "http://%s/ISAPI/ContentMgmt/search";
    private static final String DOWNLOAD = "http://%s/ISAPI/ContentMgmt/download";

    private final RestTemplate client;
    private final MarshallingService marshallingService;

    /**
     * Search camera records.
     * Create XML payload and send request, receive response and return count of finding records by params.
     *
     * @param target host
     * @return count of finding records
     */
    @Override
    @SneakyThrows
    public Integer searchContent(Target target) {
        if (target.getLogin().isEmpty() || target.getPassword().isEmpty())
            throw new EmptyCredentialsException("Empty credentials not allowed");

        log.info("searching = {}", target.getHost());

        String xmlBody;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallingService.marshalling(SearchRequestXML.class, RequestBuilder.createSearchRequest(), os);
            xmlBody = os.toString();

            log.debug(xmlBody);
        }

        String url = String.format(SEARCH, target.getHost());
        log.debug(url);

        String authString = Base64.getEncoder().encodeToString(String.format("%s:%s", target.getLogin(), target.getPassword()).getBytes());
        log.debug(authString);

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s", authString));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);

        HttpEntity<String> body = new HttpEntity<>(xmlBody, headers);
        ResponseEntity<String> response = client.postForEntity(url, body, String.class);

        log.debug(response.getBody());

        if (Objects.isNull(response.getBody()))
            throw new EmptyResponseException("Empty body response");

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
            SearchResponseXML responseXML = (SearchResponseXML) marshallingService.unmarshalling(SearchResponseXML.class, is);
            return Integer.parseInt(responseXML.getCount());
        }
    }

    /**
     * Download camera record.
     */
    @Override
    public void downloadContent() {
    }

}