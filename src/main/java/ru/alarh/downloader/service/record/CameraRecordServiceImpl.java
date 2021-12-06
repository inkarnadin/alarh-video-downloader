package ru.alarh.downloader.service.record;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.SearchResultObject;
import ru.alarh.downloader.service.record.managment.ContentManagementServiceISAPI;
import ru.alarh.downloader.service.source.SourcePrepareService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Camera record service.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CameraRecordServiceImpl implements CameraRecordService {

    private final ContentManagementServiceISAPI contentManagementService;
    private final SourcePrepareService sourcePrepareService;

    /**
     * Aggregate method for search by all targets.
     * Targets which throw exception or return zero will be skipped.
     * Metadata object will be filled meta info about search process, such as time, count and other.
     *
     * @return metadata object with searching results
     */
    @Override
    @SneakyThrows
    public MetaResult<SearchResultObject> searchByAll() {
        StopWatch sw = new StopWatch();
        sw.start();

        List<Target> targets = sourcePrepareService.readTargetFromFileSystem();

        ExecutorService executor = Executors.newFixedThreadPool(6);
        List<Callable<Void>> callables = new ArrayList<>();
        Map<Target, Integer> results = new ConcurrentHashMap<>();

        for (Target target : targets) {
            callables.add(() -> {
                try {
                    results.put(target, contentManagementService.searchContent(target));
                } catch (Exception xep) {
                    results.put(target, 0);
                }
                return null;
            });
        }
        executor.invokeAll(callables);

        List<SearchResultObject> outputList = new ArrayList<>();
        for (Map.Entry<Target, Integer> entry : results.entrySet()) {
            if (entry.getValue() != 0)
                outputList.add(new SearchResultObject(entry.getKey().getName(), entry.getKey().getHost(), entry.getValue()));
        }

        sw.stop();
        log.info("searching finished...");

        MetaResult<SearchResultObject> mr = new MetaResult<>();
        mr.setCountAll(targets.size());
        mr.setCountSuccess(outputList.size());
        mr.setDuration(Duration.ofMillis(sw.getTotalTimeMillis()));
        mr.setResults(outputList);

        return mr;
    }

}