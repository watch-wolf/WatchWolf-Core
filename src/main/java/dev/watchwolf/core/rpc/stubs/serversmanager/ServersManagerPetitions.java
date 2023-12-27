package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* Petitions managed by Servers Manager
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
*/
public interface ServersManagerPetitions {


/**
* Do nothing; just send a packet.
*/
void nop() throws java.io.IOException;

/**
* This operation allows WatchWolf Tester to start a server. It provides high customization, as you can specify plugins, worlds and config files (among other parameters).
Once a 'start server' request is received the program should create a server with the specified arguments, and return its IP:Port. The IP to send the Server Petitions is the same, but the next port (IP:<port+1>).
* @param serverType: Type of server to start.
* @param serverVersion: Server version to start.
* @param plugins: Plugins to add to the server.
* @param worldType: Defines if the world needs to be a regular one, or superflat.
* @param maps: Maps to load to the server.
* @param configFiles: Additional server config files.
* @return Started server IP and port.
If it's not possible to create it (for example: one argument is invalid, the user sent a plugin when it's specified that only Usual Plugins are allowed, or there's no free servers of that type), then an empty string is returned.
*/
java.lang.String startServer(java.lang.String serverType, java.lang.String serverVersion, java.util.Collection<dev.watchwolf.core.entities.files.plugins.Plugin> plugins, dev.watchwolf.core.entities.WorldType worldType, java.util.Collection<dev.watchwolf.core.entities.files.ConfigFile> maps, java.util.Collection<dev.watchwolf.core.entities.files.ConfigFile> configFiles) throws java.io.IOException;

}
