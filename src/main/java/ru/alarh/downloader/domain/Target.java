package ru.alarh.downloader.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Primary object which store data about remote camera host.
 *
 * @author inkarnadin
 */
@Data
@EqualsAndHashCode
@ToString
public class Target {

    private final String host;
    private final String path;
    private final String login;
    private final String password;
    private final String name;

}