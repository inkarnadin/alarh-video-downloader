package ru.alarh.downloader.service.record.managment;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alarh.downloader.configuration.web.WebClient;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.exception.EmptyCredentialsException;
import ru.alarh.downloader.exception.EmptyResponseException;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;
import ru.alarh.downloader.service.record.managment.builder.RecordManagementRequestBuilder;
import ru.alarh.downloader.service.record.managment.filesystem.FileSystemUtility;
import ru.alarh.downloader.service.record.managment.header.HeaderBuilder;
import ru.alarh.downloader.service.record.managment.template.download.DownloadRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchResponseXML;
import ru.alarh.downloader.service.xml.MarshallingService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Content manager class for working with camera records data.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ContentManagementServiceImpl implements ContentManagementService {

    private static final String SEARCH = "http://%s/ISAPI/ContentMgmt/search";
    private static final String DOWNLOAD = "http://%s/ISAPI/ContentMgmt/download";

    private final WebClient webClient;
    private final MarshallingService marshallingService;
    private final HeaderBuilder headerBuilder;
    private final RecordManagementRequestBuilder builder;

    /**
     * Search camera records. <br/>
     * Create XML payload and send request, receive response and return count of finding records by params.
     * If response body was empty thrown {@link EmptyResponseException}.
     *
     * @param target host
     * @return count of finding records
     */
    @Override
    @SneakyThrows
    public Optional<SearchResultObject> searchContent(Target target) {
        if (target.getLogin().isEmpty() || target.getPassword().isEmpty())
            throw new EmptyCredentialsException("Empty credentials not allowed");

        log.info("Searching: {}", target.getHost());

        String xmlBody;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallingService.marshalling(SearchRequestXML.class, builder.createSearchRequest(), os);
            xmlBody = os.toString();

            log.trace(xmlBody);
        }

        String url = String.format(SEARCH, target.getHost());
        log.debug(url);

        HttpEntity<String> body = new HttpEntity<>(xmlBody, headerBuilder.build(target));
        ResponseEntity<String> response = (ResponseEntity<String>) webClient.doPost(url, body, String.class);

        log.trace(response.getBody());

        if (Objects.isNull(response.getBody()))
            throw new EmptyResponseException("Empty body response");

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
            SearchResponseXML responseXML = (SearchResponseXML) marshallingService.unmarshalling(SearchResponseXML.class, is);

            if (Objects.isNull(responseXML.getMatch()) || Objects.isNull(responseXML.getMatch().getSearchMatchItem())) {
                log.info("No matches for parameters");
                return Optional.empty();
            }

            log.info("Found matches: {}", responseXML.getMatch().getSearchMatchItem().size());

            List<String> playbacks = responseXML.getMatch().getSearchMatchItem().stream()
                    .map(SearchResponseXML.Match.SearchMatchItem::getMediaSegmentDescriptor)
                    .map(SearchResponseXML.Match.SearchMatchItem.MediaSegmentDescriptor::getPlaybackURI)
                    .collect(Collectors.toList());

            return Optional.of(new SearchResultObject(
                    target.getName(),
                    target.getHost(),
                    Integer.parseInt(responseXML.getCount()),
                    playbacks));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Download camera record. <br/>
     * Credentials are verified. If they are not found, the method exits with an exception {@link EmptyCredentialsException}.
     * The request body in xml format is formed from the playback link to the downloaded video file.
     * If the download is successful, the file is saved to the directory for storing the results,
     * and the method returns {@code true}. Otherwise, the method registers the reason for the failure and returns {@code false}.
     *
     * @param target host
     * @param playback link to video file
     * @return {@code true} if download finished success, else {@code false}
     */
    @Override
    @SneakyThrows
    public boolean downloadContent(Target target, PlaybackObject playback) {
        if (target.getLogin().isEmpty() || target.getPassword().isEmpty())
            throw new EmptyCredentialsException("Empty credentials not allowed");

        log.info("Downloading: {}", playback);

        String xmlBody;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallingService.marshalling(DownloadRequestXML.class, builder.createDownloadRequest(playback), os);
            xmlBody = os.toString();

            log.trace(xmlBody);

            String url = String.format(DOWNLOAD, target.getHost());
            log.debug(url);

            HttpEntity<String> body = new HttpEntity<>(xmlBody, headerBuilder.build(target));
            ResponseEntity<byte[]> res = (ResponseEntity<byte[]>) webClient.doGetWithBody(url, body, byte[].class);

            FileSystemUtility.createDirectoryIfAbsent();
            FileSystemUtility.writeDataToFile(playback.getName(), res.getBody());
        } catch (Exception xep) {
            log.warn("Downloading finished failure: {}, reason: {}", playback.getName(), xep.getMessage());
            return false;
        }

        log.info("Downloading finished successfully: {}", playback.getName());
        return true;
    }

}