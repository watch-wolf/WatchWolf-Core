package dev.watchwolf.serversmanager.rpc;

import dev.watchwolf.core.entities.ServerType;
import dev.watchwolf.core.entities.WorldType;
import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.rpc.stubs.serversmanager.CapturedExceptionEvent;
import dev.watchwolf.core.rpc.stubs.serversmanager.ServerStartedEvent;

import java.io.IOException;

public interface ServerManagerPetition {
    public String getServersManagerVersion() throws IOException;
    public String startServer(ServerStartedEvent onServerStart, CapturedExceptionEvent onError, ServerType mcType, String version, Plugin[]plugins, WorldType worldType, ConfigFile[]configFiles) throws IOException;
}
