package dev.watchwolf.serversmanager.rpc;

import dev.watchwolf.core.entities.ServerType;
import dev.watchwolf.core.entities.WorldType;
import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.entities.files.plugins.Plugin;

import java.io.IOException;

public interface ServerManagerPetition {
    public String getServersManagerVersion() throws IOException;
    public String startServer(ServerStartNotifier onServerStart, ServerErrorNotifier onError, ServerType mcType, String version, Plugin[]plugins, WorldType worldType, ConfigFile[]configFiles) throws IOException;
}
