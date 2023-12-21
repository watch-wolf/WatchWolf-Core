package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* RPC implementation of Servers Manager to run the requests locally.
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
* 
* The Servers manager is the responsible for starting the servers with the desired configuration.
*/
public class ServersManagerLocalStub implements dev.watchwolf.core.rpc.RPCImplementer, dev.watchwolf.core.rpc.stubs.serversmanager.ServerStartedEvent, dev.watchwolf.core.rpc.stubs.serversmanager.CapturedExceptionEvent {

private dev.watchwolf.core.rpc.RPC rpc;

public void forwardCall(dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) {

}

public void setHandler(dev.watchwolf.core.rpc.RPC handler) {
this.rpc = handler;
}

/**
* The requested server was started successfully.
* Relates to `startServer` method
* Overrides method defined by interface `ServerStartedEvent`
*/
public void serverStarted() {

}

/**
* The Error notification is an async petition that it is sent to the WatchWolf Tester orchestrator as a response, without any previous petition.
* @param exception: Full stack trace of the raised error captured by the server console.
* Overrides method defined by interface `CapturedExceptionEvent`
*/
public void capturedException(java.lang.String exception) {

}

}
