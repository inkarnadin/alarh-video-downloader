package ru.alarh.downloader.service.record;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.DownloadResultObject;
import ru.alarh.downloader.service.record.dto.PlaybackObject;
import ru.alarh.downloader.service.record.dto.SearchResultObject;
import ru.alarh.downloader.service.record.managment.ContentManagementService;
import ru.alarh.downloader.service.source.SourcePrepareService;
import ru.alarh.downloader.store.PlaybackCacheHolder;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
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

    private final ContentManagementService contentManagementService;
    private final SourcePrepareService sourcePrepareService;

    /**
     * Method for search by all targets. <br/>
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
                    Optional<SearchResultObject> searchResultObject = contentManagementService.searchContent(target);

                    if (searchResultObject.isPresent()) {
                        SearchResultObject resultObject = searchResultObject.get();
                        List<PlaybackObject> playbackObjects = resultObject.getPlaybacks().stream()
                                .map(PlaybackObject::new)
                                .collect(Collectors.toList());

                        results.put(target, resultObject);
                        PlaybackCacheHolder.putPlaybacks(target, playbackObjects);
                    }
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
        log.info("Searching finished");

        MetaResult<SearchResultObject> mr = new MetaResult<>();
        mr.setCountAll(targets.size());
        mr.setCountSuccess(outputList.size());
        mr.setDuration(Duration.ofMillis(sw.getTotalTimeMillis()));
        mr.setResults(outputList);

        return mr;
    }

    /**
     * Method for search by all targets. <br/>
     * Get all targets from source file and reject all playback links by target.
     * Each playback link will start the download process.
     * It doesn't have to be concurrent because the receiving server will fail for too frequent requests. There must
     * also be a timeout between the request. Now it is 3 seconds.
     *
     * @return metadata object with downloading results
     */
    @Override
    @SneakyThrows
    public MetaResult<DownloadResultObject> downloadRecords() {
        StopWatch sw = new StopWatch();
        sw.start();

        AtomicInteger count = new AtomicInteger();
        AtomicInteger countSuccess = new AtomicInteger();

        List<DownloadResultObject> result = new ArrayList<>();

        for (Target target : PlaybackCacheHolder.getTargets()) {
            List<PlaybackObject> playbacks = PlaybackCacheHolder.getPlaybacks(target);
            count.getAndAdd(playbacks.size());

            AtomicInteger counter = new AtomicInteger();
            for (PlaybackObject playback : playbacks) {
                Thread.sleep(3000);
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    boolean isLoaded = contentManagementService.downloadContent(target, playback);
                    if (isLoaded) {
                        result.add(new DownloadResultObject(playback.getName()));
                        countSuccess.getAndIncrement();
                    }
                    return null;
                });

                future.get();
                counter.incrementAndGet();
            }
        }

        sw.stop();
        log.info("Downloading finished");

        MetaResult<DownloadResultObject> mr = new MetaResult<>();
        mr.setCountAll(count.get());
        mr.setCountSuccess(countSuccess.get());
        mr.setDuration(Duration.ofMillis(sw.getTotalTimeMillis()));
        mr.setResults(result);

        return mr;
    }

}