package ru.alarh.downloader.configuration.xml;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * Wrapper for ignore namespace during XML unmarshalling.
 *
 * @author inkarnadin
 */
public class XMLReaderWithoutNamespace extends StreamReaderDelegate {

    public XMLReaderWithoutNamespace(XMLStreamReader reader) {
        super(reader);
    }

    /**
     * Get attribute namespace.
     *
     * @param index index
     * @return attribute
     */
    @Override
    public String getAttributeNamespace(int index) {
        return "";
    }

    /**
     * Get namespace uri.
     *
     * @return uri as string
     */
    @Override
    public String getNamespaceURI() {
        return "";
    }

}