package ru.alarh.downloader.service.record.managment.builder;

import lombok.SneakyThrows;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.managment.template.download.DownloadRequestXML;
import ru.alarh.downloader.service.record.managment.template.search.SearchRequestXML;

import java.util.Collections;

/**
 * XML template builder.
 *
 * @author inkarnadin
 */
public class RecordManagementRequestBuilder {

    /**
     * Search XML template builder.
     *
     * @return object for search request
     */
    @SneakyThrows
    public static SearchRequestXML createSearchRequest() {
        SearchRequestXML requestXML = new SearchRequestXML();
        requestXML.setSearchId("C99F07A5-55C0-0001-FED3-1920796014AD");
        requestXML.setTrackIdList(Collections.singletonList(new SearchRequestXML.TrackId("101")));
        requestXML.setTimeSpanList(Collections.singletonList(
                new SearchRequestXML.TimeSpan(
                        new SearchRequestXML.TimeSpan.TimeSpanWrapper("2021-11-21T00:00:00Z", "2021-12-25T00:00:00Z")
                ))
        );
        requestXML.setResultLimit(40);
        requestXML.setPosition(0);
        requestXML.setMetadataList(Collections.singletonList(new SearchRequestXML.Metadata("//recordType.meta.std-cgi.com/VideoMotion")));

        return requestXML;
    }

    /**
     * Download XML template builder.
     *
     * @param playback playback object with url
     * @return object for download request
     */
    public static DownloadRequestXML createDownloadRequest(PlaybackObject playback) {
        DownloadRequestXML downloadRequestXML = new DownloadRequestXML();
        downloadRequestXML.setPlaybackURI(playback.getPlaybackUrl());

        return downloadRequestXML;
    }

}