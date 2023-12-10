package dev.watchwolf.core.entities.files.plugins;

import dev.watchwolf.core.entities.files.*;

import java.io.IOException;

public class PluginFactory {
    public static Plugin build(String path) throws IOException {
        if (path.startsWith("https://") || path.startsWith("http://")) return new UploadedPlugin(path);
        else if (path.contains("/")) return new FilePlugin(new ConfigFile(path));
        else return new UsualPlugin(path); // TODO version with the path
    }
}
