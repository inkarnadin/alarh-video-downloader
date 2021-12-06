package ru.alarh.downloader.service.xml;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract service for work with XML building.
 *
 * @author inkarnadin
 */
public interface MarshallingService {

    /**
     * Convert object to XML.
     *
     * @param clz class of object
     * @param obj instance of object
     * @param outputStream output data
     */
    void marshalling(Class<?> clz, Object obj, OutputStream outputStream);

    /**
     * Convert XML to object.
     *
     * @param clz class of object
     * @param inputStream input data
     * @return object of specified class
     */
    Object unmarshalling(Class<?> clz, InputStream inputStream);

}
