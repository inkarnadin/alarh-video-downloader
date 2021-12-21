package ru.alarh.downloader.service.record.managment.template.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Search response object template for parsing XML data.
 *
 * @author inkarnadin
 */
@Getter
@XmlRootElement(name = "CMSearchResult")
public class SearchResponseXML {

    @XmlElement(name = "searchID")
    private String searchId;

    @XmlElement(name = "responseStatus")
    private String responseStatus;

    @XmlElement(name = "responseStatusStrg")
    private String responseMessage;

    @XmlElement(name = "numOfMatches")
    private String count;

    @XmlElement(name = "matchList")
    private Match match;

    @Getter
    @RequiredArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Match {

        @XmlElement(name = "searchMatchItem")
        private List<SearchMatchItem> searchMatchItem;

        @Getter
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class SearchMatchItem {

            @XmlElement(name = "sourceID")
            private String sourceId;

            @XmlElement(name = "trackID")
            private String trackId;

            @XmlElement(name = "timeSpan")
            private TimeSpan timeSpan;

            @XmlElement(name = "mediaSegmentDescriptor")
            private MediaSegmentDescriptor mediaSegmentDescriptor;

            @Getter
            @XmlAccessorType(XmlAccessType.FIELD)
            public static class TimeSpan {

                @XmlElement(name = "startTime")
                private String startTime;

                @XmlElement(name = "endTime")
                private String endTime;

            }

            @Getter
            @XmlAccessorType(XmlAccessType.FIELD)
            public static class MediaSegmentDescriptor {

                @XmlElement(name = "contentType")
                private String contentType;

                @XmlElement(name = "codecType")
                private String codecType;

                @XmlElement(name = "playbackURI")
                private String playbackURI;

            }

        }

    }

    @XmlAnyElement(lax = true)
    private List<Object> trashElements;

    @XmlAnyAttribute
    private Map<Object, Object> trashAttributes;

}