package ru.alarh.downloader.service.record.managment.template.search;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Search request object template for creation XML data.
 *
 * @author inkarnadin
 */
@Setter
@XmlRootElement(name = "CMSearchDescription")
public class SearchRequestXML {

    @XmlElement(name = "searchID")
    private String searchId;

    @XmlElement(name = "trackIDList")
    private List<TrackId> trackIdList;

    @XmlElement(name = "timeSpanList")
    private List<TimeSpan> timeSpanList;

    @XmlElement(name = "maxResults")
    private int resultLimit;

    @XmlElement(name = "searchResultPosition")
    private int position;

    @XmlElement(name = "metadataList")
    private List<Metadata> metadataList;

    @RequiredArgsConstructor
    public static class TrackId {

        @XmlElement(name = "trackID")
        private final String trackId;

    }

    @RequiredArgsConstructor
    public static class TimeSpan {

        @XmlElement(name = "timeSpan")
        private final TimeSpanWrapper timeSpanWrapper;

        @RequiredArgsConstructor
        public static class TimeSpanWrapper {

            @XmlElement(name = "startTime")
            private final String startTime;

            @XmlElement(name = "endTime")
            private final String endTime;

        }

    }

    @RequiredArgsConstructor
    public static class Metadata {

        @XmlElement(name = "metadataDescriptor")
        private final String descriptor;

    }

}