package ru.alarh.downloader.service.xml;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import ru.alarh.downloader.service.record.managment.template.search.SearchResponseXML;

import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
class MarshallingServiceTest {

    @Autowired
    private MarshallingService marshallingService;

    @Test
    @SneakyThrows
    void testUnmarshallingSearchResponse() {
        InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:searchResponseXML.xml"));
        SearchResponseXML responseXML = (SearchResponseXML) marshallingService.unmarshalling(SearchResponseXML.class, is);

        Assertions.assertEquals("2", responseXML.getCount());
        Assertions.assertEquals("true", responseXML.getResponseStatus());
        Assertions.assertEquals("{C99F07A5-55C0-0001-FED3-1920796014AD}", responseXML.getSearchId());
    }

}