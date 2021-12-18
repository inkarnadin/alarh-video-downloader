package ru.alarh.downloader.service.record.managment;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.alarh.downloader.configuration.web.WebClient;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.exception.EmptyCredentialsException;
import ru.alarh.downloader.exception.EmptyResponseException;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;
import ru.alarh.downloader.service.record.managment.builder.RecordManagementRequestBuilder;
import ru.alarh.downloader.service.record.managment.template.download.DownloadRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchResponseXML;
import ru.alarh.downloader.service.xml.MarshallingService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Content manager class for working with camera records data.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentManagementServiceImpl implements ContentManagementService {

    private static final String SEARCH = "http://%s/ISAPI/ContentMgmt/search";
    private static final String DOWNLOAD = "http://%s/ISAPI/ContentMgmt/download";

    private final WebClient webClient;
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
    public SearchResultObject searchContent(Target target) {
        if (target.getLogin().isEmpty() || target.getPassword().isEmpty())
            throw new EmptyCredentialsException("Empty credentials not allowed");

        log.info("searching = {}", target.getHost());

        String xmlBody;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallingService.marshalling(SearchRequestXML.class, RecordManagementRequestBuilder.createSearchRequest(), os);
            xmlBody = os.toString();

            log.trace(xmlBody);
        }

        String url = String.format(SEARCH, target.getHost());
        log.debug(url);

        HttpEntity<String> body = new HttpEntity<>(xmlBody, buildHeaders(target));
        ResponseEntity<String> response = (ResponseEntity<String>) webClient.doPost(url, body, String.class);

        log.trace(response.getBody());

        if (Objects.isNull(response.getBody()))
            throw new EmptyResponseException("Empty body response");

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
            SearchResponseXML responseXML = (SearchResponseXML) marshallingService.unmarshalling(SearchResponseXML.class, is);

            List<String> playbacks = responseXML.getMatch().getSearchMatchItem().stream()
                    .map(SearchResponseXML.Match.SearchMatchItem::getMediaSegmentDescriptor)
                    .map(SearchResponseXML.Match.SearchMatchItem.MediaSegmentDescriptor::getPlaybackURI)
                    .collect(Collectors.toList());

            return new SearchResultObject(
                    target.getName(),
                    target.getHost(),
                    Integer.parseInt(responseXML.getCount()),
                    playbacks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Download camera record.
     */
    @Override
    @SneakyThrows
    public void downloadContent(Target target, PlaybackObject playback) {
        if (target.getLogin().isEmpty() || target.getPassword().isEmpty())
            throw new EmptyCredentialsException("Empty credentials not allowed");

        log.info("downloading = {}", playback);

        String xmlBody;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallingService.marshalling(DownloadRequestXML.class, RecordManagementRequestBuilder.createDownloadRequest(playback), os);
            xmlBody = os.toString();

            log.trace(xmlBody);

            String url = String.format(DOWNLOAD, target.getHost());
            log.debug(url);

            HttpEntity<String> body = new HttpEntity<>(xmlBody, buildHeaders(target));
            ResponseEntity<byte[]> res = (ResponseEntity<byte[]>) webClient.doGetWithBody(url, body, byte[].class);

            Files.write(
                    Paths.get(String.format("result/%s.mp4", playback.getName())),
                    Objects.requireNonNull(res.getBody())
            );
        }
    }

    private MultiValueMap<String, String> buildHeaders(Target target) {
        String authString = Base64.getEncoder().encodeToString(String.format("%s:%s", target.getLogin(), target.getPassword()).getBytes());
        log.debug(authString);

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s", authString));
        headers.add(HttpHeaders.CONNECTION, "keep-alive");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);

        return headers;
    }

}