package ru.alarh.downloader.store;

import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Playbacks link holder.
 *
 * @author inkarnadin
 */
public final class PlaybackCacheHolder {

    private final static Map<Target, List<PlaybackObject>> playbackMap = new ConcurrentHashMap<>();

    private PlaybackCacheHolder() {
        throw new AssertionError("Utility class can't be creation");
    }

    /**
     * Get all playback links by target instance.
     *
     * @param target remote host object
     * @return list of playback links
     */
    public static List<PlaybackObject> getPlaybacks(Target target) {
        return playbackMap.get(target);
    }

    /**
     * Put new playback links by target instance.
     *
     * @param target remote host object
     * @param playbacks list of playback links
     */
    public static void putPlaybacks(Target target, List<PlaybackObject> playbacks) {
        playbackMap.put(target, playbacks);
    }

    /**
     * Remove playback links by target.
     *
     * @param target remote host object
     */
    public static void remove(Target target) {
        playbackMap.remove(target);
    }

}