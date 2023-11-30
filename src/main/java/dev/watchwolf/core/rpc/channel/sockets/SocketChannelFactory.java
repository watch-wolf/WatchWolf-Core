package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.ChannelFactory;
import dev.watchwolf.core.rpc.channel.MessageChannel;

public class SocketChannelFactory implements ChannelFactory {
    private final String host;
    private final int port;

    public SocketChannelFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public MessageChannel build() {
        return new SocketMessageChannel(this.host, this.port);
    }
}
