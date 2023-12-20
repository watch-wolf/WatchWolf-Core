package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* RPC implementation of Servers Manager to run the requests locally.
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
* 
* The Servers manager is the responsible for starting the servers with the desired configuration.
*/
class ServersManagerLocalStub implements dev.watchwolf.core.rpc.RPCImplementer {

private dev.watchwolf.core.rpc.RPC rpc;

public void forwardCall(dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) {

}

public void setHandler(dev.watchwolf.core.rpc.RPC handler) {
this.rpc = handler;
}

}
