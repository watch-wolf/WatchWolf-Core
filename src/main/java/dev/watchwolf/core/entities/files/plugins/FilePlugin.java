package dev.watchwolf.core.entities.files.plugins;

import dev.watchwolf.core.entities.files.ConfigFile;

import java.util.ArrayList;

public class FilePlugin extends Plugin {
    private final ConfigFile file;

    public FilePlugin(ConfigFile f) {
        this.file = f;
    }

    public ConfigFile getFile() {
        return this.file;
    }
}
