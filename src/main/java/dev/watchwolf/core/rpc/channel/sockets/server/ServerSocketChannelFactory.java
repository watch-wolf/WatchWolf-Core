package dev.watchwolf.core.rpc.channel.sockets.server;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketChannelFactory;

public class ServerSocketChannelFactory extends SocketChannelFactory {
    public ServerSocketChannelFactory(String host, int port) {
        super(host, port);
    }

    @Override
    public MessageChannel build() {
        return new ServerSocketMessageChannel(this.host, this.port);
    }
}
