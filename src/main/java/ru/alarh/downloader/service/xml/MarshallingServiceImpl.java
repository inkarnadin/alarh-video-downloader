package ru.alarh.downloader.service.xml;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.alarh.downloader.configuration.xml.XMLReaderWithoutNamespace;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Service for work with XML.
 *
 * @author inkarnadin
 */
@Service
public class MarshallingServiceImpl implements MarshallingService {

    /**
     * Convert object to XML.
     *
     * @param clz class of object
     * @param obj instance of object
     * @param outputStream output data
     */
    @Override
    @SneakyThrows
    public void marshalling(Class<?> clz, Object obj, OutputStream outputStream) {
        JAXBContext context = JAXBContext.newInstance(clz);
        Marshaller marshaller= context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(obj, outputStream);
    }

    /**
     * Convert XML to object.
     * Ignored XML namespace.
     *
     * @param clz class of object
     * @param inputStream input data
     * @return object of specified class
     */
    @Override
    @SneakyThrows
    public Object unmarshalling(Class<?> clz, InputStream inputStream) {
        JAXBContext context = JAXBContext.newInstance(clz);

        XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(inputStream);
        XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);

        return context.createUnmarshaller().unmarshal(xr);
    }

}