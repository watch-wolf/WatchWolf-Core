package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.ChannelFactory;
import dev.watchwolf.core.rpc.objects.converter.RPCObjectsConverterFactory;

public class RPCFactory {
    public RPC build(RPCImplementerFactory localImplementationFactory, ChannelFactory remoteConnectionFactory) {
        RPCObjectsConverterFactory rpcObjectsConverterFactory = new RPCObjectsConverterFactory();
        return new RPC(localImplementationFactory.build(), remoteConnectionFactory.build(), rpcObjectsConverterFactory.build());
    }
}
