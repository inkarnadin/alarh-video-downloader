package ru.alarh.downloader.domain;

import lombok.Data;

/**
 * Primary object which store data about camera host.
 *
 * @author inkarnadin
 */
@Data
public class Target {

    private final String host;
    private final String path;
    private final String login;
    private final String password;
    private final String name;

}