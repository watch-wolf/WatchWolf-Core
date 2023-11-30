package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.ChannelFactory;
import dev.watchwolf.core.rpc.objects.converter.RPCObjectsConverter;

public class RPCFactory {
    public RPC build(RPCImplementerFactory localImplementationFactory, ChannelFactory remoteConnectionFactory) {
        return new RPC(localImplementationFactory.build(), remoteConnectionFactory.build(), new RPCObjectsConverter());
    }
}
