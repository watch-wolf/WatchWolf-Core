package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.ChannelFactory;
import dev.watchwolf.core.rpc.channel.MessageChannel;

public abstract class SocketChannelFactory implements ChannelFactory {
    protected final String host;
    protected final int port;

    public SocketChannelFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public abstract MessageChannel build();
}
