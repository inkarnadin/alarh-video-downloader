package ru.alarh.downloader.service.record;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;
import ru.alarh.downloader.service.record.managment.ContentManagementServiceImpl;
import ru.alarh.downloader.service.source.SourcePrepareService;
import ru.alarh.downloader.store.PlaybackCacheHolder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Camera record service.
 *
 * @author inkarnadin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CameraRecordServiceImpl implements CameraRecordService {

    private final ExecutorService executor = Executors.newFixedThreadPool(6);

    private final ContentManagementServiceImpl contentManagementService;
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
        List<Callable<Void>> callables = new ArrayList<>();
        Map<Target, SearchResultObject> results = new ConcurrentHashMap<>();

        for (Target target : targets) {
            callables.add(() -> {
                try {
                    SearchResultObject searchResultObject = contentManagementService.searchContent(target);
                    List<PlaybackObject> playbackObjects = searchResultObject.getPlaybacks().stream()
                            .map(PlaybackObject::new)
                            .collect(Collectors.toList());

                    results.put(target, searchResultObject);
                    PlaybackCacheHolder.putPlaybacks(target, playbackObjects);
                } catch (Exception xep) {
                    results.put(target, null);
                }
                return null;
            });
        }
        executor.invokeAll(callables);

        List<SearchResultObject> outputList = new ArrayList<>();
        for (Map.Entry<Target, SearchResultObject> entry : results.entrySet())
            if (Objects.nonNull(entry.getValue()))
                outputList.add(entry.getValue());

        sw.stop();
        log.info("searching finished...");

        MetaResult<SearchResultObject> mr = new MetaResult<>();
        mr.setCountAll(targets.size());
        mr.setCountSuccess(outputList.size());
        mr.setDuration(Duration.ofMillis(sw.getTotalTimeMillis()));
        mr.setResults(outputList);

        return mr;
    }

    @Override
    @SneakyThrows
    public MetaResult<Void> downloadRecords() {
        StopWatch sw = new StopWatch();
        sw.start();

        List<Target> targets = sourcePrepareService.readTargetFromFileSystem();
        List<Callable<Void>> callables = new ArrayList<>();

        AtomicInteger count = new AtomicInteger();
        AtomicInteger countSuccess = new AtomicInteger();

//        for (Target target : targets) {
//            List<PlaybackObject> playbacks = PlaybackCacheHolder.getPlaybacks(target);
//            count.getAndAdd(playbacks.size());
//
//            for (PlaybackObject playback : playbacks) {
//                callables.add(() -> {
//                    contentManagementService.downloadContent(target, playback);
//                    countSuccess.getAndIncrement();
//                    return null;
//                });
//            }
//            //PlaybackCacheHolder.remove(target);
//        }
//        executor.invokeAll(callables);

        for (Target target : targets) {
            List<PlaybackObject> playbacks = PlaybackCacheHolder.getPlaybacks(target);
            count.getAndAdd(playbacks.size());

            for (PlaybackObject playback : playbacks) {
                contentManagementService.downloadContent(target, playback);
                Thread.sleep(1000);
                countSuccess.getAndIncrement();
            }
        }

        sw.stop();
        log.info("downloading finished...");

        MetaResult<SearchResultObject> mr = new MetaResult<>();
        mr.setCountAll(count.get());
        mr.setCountSuccess(countSuccess.get());
        mr.setDuration(Duration.ofMillis(sw.getTotalTimeMillis()));
        mr.setResults(null);

        return null;
    }

}