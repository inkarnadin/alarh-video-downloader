package ru.alarh.downloader.service.record.managment.template.download;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Download request object template for creation XML data.
 *
 * @author inkarnadin
 */
@Setter
@XmlRootElement(name = "downloadRequest")
public class DownloadRequestXML {

    @XmlElement(name = "playbackURI")
    private String playbackURI;

}