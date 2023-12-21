package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* Event produced by Servers Manager
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
*/
public interface ServerStartedEvent {


/**
* The requested server was started successfully.
* Relates to `startServer` method
*/
void serverStarted() throws java.io.IOException;

}
