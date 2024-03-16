package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.ChannelFactory;
import dev.watchwolf.core.rpc.objects.converter.RPCObjectsConverterFactory;

public class RPCFactory {
    public RPC build(RPCImplementerFactory localImplementationFactory, ChannelFactory remoteConnectionFactory) {
        RPCObjectsConverterFactory rpcObjectsConverterFactory = new RPCObjectsConverterFactory();
        RPCImplementer implementer = localImplementationFactory.build();

        RPC built = new RPC(implementer, remoteConnectionFactory.build(), rpcObjectsConverterFactory.build());
        implementer.setHandler(built); // to forward the events

        return built;
    }
}
