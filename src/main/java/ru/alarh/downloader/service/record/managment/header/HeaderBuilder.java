package ru.alarh.downloader.service.record.managment.header;

import org.springframework.util.MultiValueMap;
import ru.alarh.downloader.domain.Target;

/**
 * Common request headers builder interface.
 *
 * @author inkarnadin
 */
public interface HeaderBuilder {

    /**
     * Build headers map.
     *
     * @param target remote host
     * @return headers
     */
    MultiValueMap<String, String> build(Target target);

}
