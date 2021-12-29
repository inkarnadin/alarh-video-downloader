package ru.alarh.downloader.service.record.managment.builder;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.alarh.downloader.configuration.properties.SearchProperties;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.managment.template.download.DownloadRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchRequestXML;

import java.util.Collections;

/**
 * XML template builder.
 *
 * @author inkarnadin
 */
@Component
@RequiredArgsConstructor
public class RecordManagementRequestBuilder {

    private final SearchProperties properties;

    /**
     * Search XML template builder.
     *
     * @return object for search request
     */
    @SneakyThrows
    public SearchRequestXML createSearchRequest() {
        SearchRequestXML requestXML = new SearchRequestXML();
        requestXML.setSearchId("C99F07A5-55C0-0001-FED3-1920796014AD");
        requestXML.setTrackIdList(Collections.singletonList(new SearchRequestXML.TrackId("101")));
        requestXML.setTimeSpanList(Collections.singletonList(
                new SearchRequestXML.TimeSpan(
                        new SearchRequestXML.TimeSpan.TimeSpanWrapper(properties.getStartDate(), properties.getEndDate())
                ))
        );
        requestXML.setResultLimit(properties.getResultLimit());
        requestXML.setPosition(properties.getStartPosition());
        requestXML.setMetadataList(Collections.singletonList(new SearchRequestXML.Metadata("//recordType.meta.std-cgi.com/VideoMotion")));

        return requestXML;
    }

    /**
     * Download XML template builder.
     *
     * @param playback playback object with url
     * @return object for download request
     */
    public DownloadRequestXML createDownloadRequest(PlaybackObject playback) {
        DownloadRequestXML downloadRequestXML = new DownloadRequestXML();
        downloadRequestXML.setPlaybackURI(playback.getPlaybackUrl());

        return downloadRequestXML;
    }

}