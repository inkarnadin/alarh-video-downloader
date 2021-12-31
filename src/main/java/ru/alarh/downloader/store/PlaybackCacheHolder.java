package ru.alarh.downloader.store;

import lombok.experimental.UtilityClass;
import ru.alarh.downloader.domain.Target;
import ru.alarh.downloader.service.record.dto.PlaybackObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Playbacks link holder.
 *
 * @author inkarnadin
 */
@UtilityClass
public final class PlaybackCacheHolder {

    private final static Map<Target, List<PlaybackObject>> playbackMap = new ConcurrentHashMap<>();

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

    /**
     * Get all targets with valid playbacks.
     *
     * @return list of target
     */
    public static Set<Target> getTargets() {
        return playbackMap.keySet();
    }

}