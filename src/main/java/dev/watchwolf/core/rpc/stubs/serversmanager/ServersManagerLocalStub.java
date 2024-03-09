package dev.watchwolf.core.rpc.stubs.serversmanager;

/**
* RPC implementation of Servers Manager to run the requests locally.
* /!\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\
* 
* The Servers manager is the responsible for starting the servers with the desired configuration.
*/
public class ServersManagerLocalStub implements dev.watchwolf.core.rpc.RPCImplementer, dev.watchwolf.core.rpc.stubs.serversmanager.ServerStartedEvent, dev.watchwolf.core.rpc.stubs.serversmanager.CapturedExceptionEvent {

/**
* The RPC node will be the one that will run the events raised
*/
private dev.watchwolf.core.rpc.RPC rpc;
/**
* The runner will be the one that will run the petitions captured
*/
private dev.watchwolf.core.rpc.stubs.serversmanager.ServersManagerPetitions runner;
/**
* Latest MessageChannel petition got
*/
private dev.watchwolf.core.rpc.channel.MessageChannel latestMessageChannelPetition;

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

public void setRunner(dev.watchwolf.core.rpc.stubs.serversmanager.ServersManagerPetitions runner) {
	this.runner = runner;
}

public synchronized dev.watchwolf.core.rpc.channel.MessageChannel getLatestMessageChannelPetitionNode() {
	return this.latestMessageChannelPetition;
}

private void forwardCall(byte origin, boolean isReturn, short operation, dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) throws java.io.IOException {
	if (origin == 1 /* WW server is the origin */ && isReturn && operation == 2 /* 'server started' return */) {
		// legacy call; read arguments and do nothing
		converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString.class);
		return;
	}

	// arg guards
	if (origin != 0) throw new java.lang.RuntimeException("Got a request targeting a different component");
	if (isReturn) throw new java.lang.RuntimeException("Got a return instead of a request");

	this.latestMessageChannelPetition = channel;

	// operation calls
	if (operation == 0) nop(channel, converter);
	else if (operation == 1) startServer(channel, converter);
	else throw new java.lang.UnsupportedOperationException("Got unsupported operation: " + operation); // no match
}

/**
* Do nothing; just send a packet.
*/
private void nop(dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) throws java.io.IOException {
	this.runner.nop();
}

/**
* This operation allows WatchWolf Tester to start a server. It provides high customization, as you can specify plugins, worlds and config files (among other parameters).
Once a 'start server' request is received the program should create a server with the specified arguments, and return its IP:Port. The IP to send the Server Petitions is the same, but the next port (IP:<port+1>).
* This method will extract the following parameters for the `runner`:
* - serverType: Type of server to start.
* - serverVersion: Server version to start.
* - plugins: Plugins to add to the server.
* - worldType: Defines if the world needs to be a regular one, or superflat.
* - maps: Maps to load to the server.
* - configFiles: Additional server config files.
* This method will return: IP - Started server IP and port.
If it's not possible to create it (for example: one argument is invalid, the user sent a plugin when it's specified that only Usual Plugins are allowed, or there's no free servers of that type), then an empty string is returned.
*/
private void startServer(dev.watchwolf.core.rpc.channel.MessageChannel channel, dev.watchwolf.core.rpc.objects.converter.RPCConverter<?> converter) throws java.io.IOException {
	java.lang.String serverType = converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString.class).getObject();
	java.lang.String serverVersion = converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString.class).getObject();
	java.util.Collection<dev.watchwolf.core.entities.files.plugins.Plugin> plugins = converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory.getTemplateType(dev.watchwolf.core.rpc.objects.types.natives.composited.RPCArray.class, dev.watchwolf.core.rpc.objects.types.custom.files.plugins.RPCPlugin.class))
					.getObject(converter, dev.watchwolf.core.entities.files.plugins.Plugin.class);
	dev.watchwolf.core.entities.WorldType worldType = (dev.watchwolf.core.entities.WorldType)converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.types.natives.composited.RPCEnum.class)
					.getObject(dev.watchwolf.core.entities.WorldType.class);
	java.util.Collection<dev.watchwolf.core.entities.files.ConfigFile> maps = converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory.getTemplateType(dev.watchwolf.core.rpc.objects.types.natives.composited.RPCArray.class, dev.watchwolf.core.rpc.objects.types.custom.files.RPCConfigFile.class))
					.getObject(converter, dev.watchwolf.core.entities.files.ConfigFile.class);
	java.util.Collection<dev.watchwolf.core.entities.files.ConfigFile> configFiles = converter.unmarshall(channel, dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory.getTemplateType(dev.watchwolf.core.rpc.objects.types.natives.composited.RPCArray.class, dev.watchwolf.core.rpc.objects.types.custom.files.RPCConfigFile.class))
					.getObject(converter, dev.watchwolf.core.entities.files.ConfigFile.class);

	java.lang.String ip = this.runner.startServer(serverType, serverVersion, plugins, worldType, maps, configFiles);

	new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0001_1_000).send(channel);
	new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000).send(channel);
	new dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString(ip).send(channel);
}

/**
* The requested server was started successfully.
* Relates to `startServer` method
* Overrides method defined by interface `ServerStartedEvent`
*/
public void serverStarted() throws java.io.IOException {
	if (this.rpc == null) throw new java.lang.RuntimeException("Got 'send event' call before RPC instance");
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
	if (this.rpc == null) throw new java.lang.RuntimeException("Got 'send event' call before RPC instance");
	synchronized (this) {
		this.rpc.sendEvent(
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0011_1_000),
				new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000),
				new dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString(exception)
		);
	}
}

}
