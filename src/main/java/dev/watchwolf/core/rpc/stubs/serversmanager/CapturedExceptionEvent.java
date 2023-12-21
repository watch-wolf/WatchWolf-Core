package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* Event produced by Servers Manager
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
*/
public interface CapturedExceptionEvent {


/**
* The Error notification is an async petition that it is sent to the WatchWolf Tester orchestrator as a response, without any previous petition.
* @param exception: Full stack trace of the raised error captured by the server console.
*/
void capturedException(java.lang.String exception) throws java.io.IOException;

}
