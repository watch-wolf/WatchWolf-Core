package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* RPC implementation of Servers Manager to run the requests locally.
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
* 
* The Servers manager is the responsible for starting the servers with the desired configuration.
*/
public class ServersManagerLocalStub implements dev.watchwolf.core.rpc.RPCImplementer, dev.watchwolf.core.rpc.stubs.serversmanager.ServerStartedEvent, dev.watchwolf.core.rpc.stubs.serversmanager.CapturedExceptionEvent {

private dev.watchwolf.core.rpc.RPC rpc;

public void forwardCall(dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) throws java.io.IOException {
	synchronized (this) {
		short info = converter.unmarshall(channel, Short.class);
		byte origin = (byte)(info & 0b111);
		boolean isReturn = (info & 0b1_000) > 0;
		short operation = (short)(info >> 4);
		this.forwardCall(origin, isReturn, operation, channel, converter);
	}
}

public void setHandler(dev.watchwolf.core.rpc.RPC handler) {
	this.rpc = handler;
}

private void forwardCall(byte origin, boolean isReturn, short operation, dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) throws java.io.IOException {

}

/**
* The requested server was started successfully.
* Relates to `startServer` method
* Overrides method defined by interface `ServerStartedEvent`
*/
public void serverStarted() throws java.io.IOException {
	if (this.rpc == null) throw new java.lang.RuntimeException("Send event call before RPC instance");
	synchronized (this) {
		this.rpc.sendEvent(
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0010_1_000),
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000)
		);
	}
}

/**
* The Error notification is an async petition that it is sent to the WatchWolf Tester orchestrator as a response, without any previous petition.
* @param exception: Full stack trace of the raised error captured by the server console.
* Overrides method defined by interface `CapturedExceptionEvent`
*/
public void capturedException(java.lang.String exception) throws java.io.IOException {
	if (this.rpc == null) throw new java.lang.RuntimeException("Send event call before RPC instance");
	synchronized (this) {
		this.rpc.sendEvent(
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0011_1_000),
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000),
				new dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString(exception)
		);
	}
}

}
