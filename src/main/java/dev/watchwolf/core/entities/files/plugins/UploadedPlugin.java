package dev.watchwolf.core.entities.files.plugins;

import java.util.ArrayList;

public class UploadedPlugin extends Plugin {
    private final String url;

    public UploadedPlugin(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
