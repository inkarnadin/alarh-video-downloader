package ru.alarh.downloader.store;

import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PlaybackCacheHolder {

    private final static Map<Target, List<PlaybackObject>> playbackMap = new ConcurrentHashMap<>();

    public static List<PlaybackObject> getPlaybacks(Target target) {
        return playbackMap.get(target);
    }

    public static void putPlaybacks(Target target, List<PlaybackObject> playbacks) {
        playbackMap.put(target, playbacks);
    }

    public static void remove(Target target) {
        playbackMap.remove(target);
    }

}