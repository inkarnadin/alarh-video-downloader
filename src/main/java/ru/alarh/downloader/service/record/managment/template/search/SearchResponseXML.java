package ru.alarh.downloader.service.record.managment.template.search;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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

    @XmlAnyElement(lax = true)
    private List<Object> trashElements;

    @XmlAnyAttribute
    private Map<Object, Object> trashAttributes;

}