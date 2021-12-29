package ru.alarh.downloader.service.record.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Playback link wrapper.
 *
 * @author inkarnadin
 */
@Data
public class PlaybackObject {

    private final String playbackUrl;
    private final String name;

    public PlaybackObject(String playbackUrl) {
        this.playbackUrl = playbackUrl;

        Pattern pattern = Pattern.compile("name=(.*)&");
        Matcher matcher = pattern.matcher(playbackUrl);

        this.name = (matcher.find())
                ? matcher.group(1)
                : "unknown_" + LocalTime.now().toString();
    }

}